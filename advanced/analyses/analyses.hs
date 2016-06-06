import Data.List
import System.Directory
--import Control.Exception
import Data.Maybe
import Data.CSV.Conduit
import qualified Data.Map.Lazy as Map
import qualified Data.Vector as V
import Graphics.Rendering.Chart.Easy
-- import Graphics.Rendering.Chart.Backend.Diagrams
import Graphics.Rendering.Chart.Backend.Cairo
import Graphics.Rendering.Chart
import Data.Default.Class
import Data.Colour
import Data.Colour.Names
import Control.Monad
import Control.Lens
import System.FilePath.Lens
import qualified Data.Text as T
import qualified Data.Text.IO as TIO


-- Count lines in a text file. 
countLines :: FilePath -> IO Int
countLines f = do
    content <- TIO.readFile f
    let nonEmptyLines = filter (\x -> strip x /= ""  ) $ lines $ T.unpack content 
    return $ length nonEmptyLines

strip :: String -> String
strip = T.unpack . T.strip . T.pack

-- PSE generates csv files where each line represents the number of individuals
-- in the population. The population size is thus computed as the number of
-- lines in the csv file minus the header line.

popSize :: FilePath -> IO Int 
popSize f = fmap (\x -> x - 1) (countLines f)

-- Gives the paths to the population files in a given directory and their
-- generation.

popFiles :: FilePath -> IO [(Int, FilePath)]
popFiles dir = do
    files <- getDirectoryContents dir
    let zipgenfile = catMaybes $ map (\f -> fileGeneration f >>= (\g -> return (g,dir ++ f))) files
    let sorted = sortBy increasingGeneration zipgenfile 
    return sorted
    where increasingGeneration (a, _) (b, _) = compare a b 

-- Returns the generation number of a populationfile formated as population123.csv from its filename

fileGeneration :: String -> Maybe Int
fileGeneration s = do
    prefixStripped <- T.stripPrefix (T.pack "population") $ T.pack (s ^. filename)
    suffixStripped <- T.stripSuffix (T.pack ".csv") $ prefixStripped
    return $ read $ T.unpack suffixStripped

-- Computes the volume discovered by PSE as a function of the generation for
-- each file provided.  The returned tuples are of the form (generation, volume
-- size)

volumeDiscovered :: [(Int, FilePath)] -> IO [(Int, Int)]
volumeDiscovered popfiles = do
    mapM (\(g,f) -> (popSize f) >>= (\a -> return $ (g,a))) popfiles

-- Read a population file

readPopFile :: (MapRow T.Text -> i) -> FilePath -> IO [i]
readPopFile rowToIndiv f = do
    csvData <- (readCSVFile defCSVSettings f :: IO (V.Vector (MapRow T.Text)))
    return $ fmap rowToIndiv (V.toList csvData)

-- Read several population files

readPopFiles :: (MapRow T.Text -> i) -> [(Int, FilePath)] -> IO [(Int,i)]
readPopFiles readRow popfiles = (fmap join) . sequence . (map readOneFile) $ popfiles 
    where readOneFile (i, f) = readPopFile (\r -> (i,readRow r)) f

-- Return the value of a single field from a row

fromRow :: (Read a) => String -> MapRow T.Text -> a
fromRow field row = read . T.unpack $ row Map.! (T.pack field)

-- The fitnesses of all individuals in the given population as a function of
-- the evaluations (= number of simulations executed = number of generations in
-- a steady state evolutionary model). Fitnesses are given by the field
-- foodDifference, which represents the sum of the absolute difference between
-- the times in which the different food sources were depleted in a simulation
-- and the corresponding times measured in an imaginary simulation: 
-- |250 - simuFood1| + |400 - simuFood2| + |800 - simuFood3|

fitnessesAndEvalsAnts :: (Int, FilePath) -> IO [(Int, Double)]
fitnessesAndEvalsAnts (generation,f) = do
    fitnesses <- readPopFile (\r -> fromRow "foodDifference" r) f 
    return [(generation,x) | x <- fitnesses]

-- The fitnesses as a function of the evaluations for all the population
-- provided

fitnessesVSEvals :: [(Int, FilePath)] -> IO [(Int, Double)]
fitnessesVSEvals popfiles = (fmap join) . sequence . (map fitnessesAndEvalsAnts) $ popfiles

-- The last population file in a directory.

lastPopFile :: FilePath -> IO (Maybe FilePath)
lastPopFile dir = do
    pf <- popFiles dir
    return $ case pf of
        [] -> Nothing
        _ -> (Just . snd . last) pf

-- Plot patterns in two dimensions: horizontally the time in which the first (closest) food source
-- was depleted, and vertically the time in which the third (farthest) food source was depleted.

plotPatterns :: FilePath -> [(Double, Double)] -> IO (PickFn ())
plotPatterns file d = renderableToFile (FileOptions (400,400) PNG) file $ toRenderable layout
    where
        patternsPlot = plot_points_style .~ filledCircles 2 (opaque blue)
                     $ plot_points_values .~ d
                     $ def
        limits = PlotHidden {_plot_hidden_x_values = [-0.0,2000.0]
                            ,_plot_hidden_y_values = [-0.0,2000.0]}
        layout = layout_title .~ "Patterns"
               $ layout_x_axis . laxis_title .~ "consumption time source 1"
               $ layout_y_axis . laxis_title .~ "consumption time source 3"
               $ layout_plots .~ [toPlot patternsPlot, toPlot limits]
               -- $ layout_x_axis . laxis_generate .~ scaledAxis def (-0.1, 1.1)
               -- $ layout_y_axis . laxis_generate .~ scaledAxis def (-0.1, 1.1)
               -- $ layout_x_axis . laxis_style . axis_label_gap .~ 20
               $ layout_bottom_axis_visibility . axis_show_line .~ False
               $ layout_left_axis_visibility . axis_show_line .~ False
               $ layout_bottom_axis_visibility . axis_show_ticks .~ False
               $ layout_left_axis_visibility . axis_show_ticks .~ False
               $ def

-- Plot the volume discovered by PSE as a function of the number of evaluations

plotVolumeDiscovered :: FilePath -> [(Int, Int)] -> IO ()
plotVolumeDiscovered file vd = toFile (FileOptions (500,300) PNG) file $ do
    layout_title .= "PSE"
    -- (layout_x_axis . laxis_title) .= "évaluations"
    -- (layout_y_axis . laxis_title) .= "nombre de motifs découverts"
    (layout_x_axis . laxis_title) .= "evaluations"
    (layout_y_axis . laxis_title) .= "number of patterns discovered"
    setColors [opaque blue]
    plot (line "" [vd])

-- Plot the fitnesses of individuals during the model calibration as a function
-- of the number of evaluations.

plotFitnessVSEvaluations :: FilePath -> (Double, Double)-> [(Int, Double)] -> IO ()
plotFitnessVSEvaluations file (ymin,ymax) vd = toFile (FileOptions (500,300) PNG) file $ do
    layout_title .= "Calibration"
    (layout_x_axis . laxis_title) .= "evaluations"
    (layout_y_axis . laxis_title) .= "distance between experiment and simulation"
    setColors [opaque blue]
    plot (points "" (filter (\(e,f) -> (f <= ymax) && (f >= ymin)) vd))

-- Plot the profile of the given parameter

plotProfile :: String -> FilePath -> (Double, Double) -> (Double, Double) -> [(Double, Double)] -> IO ()
plotProfile parname file (xmin, xmax) (ymin, ymax) xy = toFile (FileOptions (500,300) PNG) file $ do
    layout_title .= "Profile " ++ parname
    (layout_x_axis . laxis_title) .= parname
    (layout_y_axis . laxis_title) .= "distance between experiment and simulation"
    -- (layout_y_axis . laxis_title) .= "distance experience/simulation"
    setColors [opaque blue]
    plot (points "" (filter (\(x,y) -> (x >= xmin && x <= xmax && y >= ymin && y <= ymax)) xy))
    setColors [transparent]
    plot (points "" [(xmin, xmax),(xmax, ymax)])


-- Plot all figures for the calibration experiment.

plot_ants_calibrate = do
    popfiles <- popFiles "../ants_calibrate/results/"
    res <- fitnessesVSEvals popfiles
    let groupedFitByGeneration = groupBy (\(g1, _) (g2, _) -> g1 == g2) res
    let bestFitness fs = minimumBy (\(_, f1) (_,f2) -> compare f1 f2)  fs
    let bestfitnesses = map bestFitness groupedFitByGeneration
    plotFitnessVSEvaluations "../ants_calibrate/fitnessVSEval.png" (0,2000) res
    putStrLn ("Written " ++ "../ants_calibrate/fitnessVSEval.png")

-- Plot all figures for the PSE experiment.

plot_ants_pse = do
    popfiles <- popFiles "../ants_pse/results/"
    res <- volumeDiscovered popfiles
    plotVolumeDiscovered "../ants_pse/volumeDiscovered.png" res
    putStrLn ("Written " ++ "../ants_pse/volumeDiscovered.png")
    maybelpf <- lastPopFile "../ants_pse/results/"
    case maybelpf of 
        (Just lpf) -> do
            patterns <- readPopFile (\r -> (fromRow "medFood1" r, fromRow "medFood3" r)) lpf
            res <- plotPatterns "../ants_pse/patterns.png" patterns
            putStrLn ("Written " ++ "../ants_pse/patterns.png")
            return res
        Nothing -> do 
            putStrLn "No file found."
            undefined

-- Plot all figures for the profiles experiment.

plot_ants_profiles = do
    plotAntsProfLast 0 "diffusion"
    plotAntsProfLast 1 "evaporation"
    where 
        plotAntsProf i name = do
            popfiles <- popFiles ("../ants_profiles/results/"++ (show i) ++ "/")
            xy <- readPopFiles (\r -> (fromRow name r, fromRow "foodDifference" r)) popfiles
            plotProfile name ("../ants_profiles/profile_" ++ name ++ ".png") xlim ylim (map snd xy)
        plotAntsProfLast i name = do
            maybelpf <- lastPopFile ("../ants_profiles/results/"++ (show i) ++ "/")
            case maybelpf of
                (Just lpf) -> do
                    xy <- readPopFile (\r -> (fromRow name r, fromRow "foodDifference" r)) lpf
                    res <- plotProfile name ("../ants_profiles/profile_" ++ name ++ ".png") xlim ylim xy
                    putStrLn ("Written " ++ "../ants_profiles/profile_" ++ name ++ ".png")
                    return res
                Nothing -> do
                    putStrLn ("No file found for profile " ++ name ++ ", " ++ (show i))
                    return ()
        xlim = (0,99) 
        ylim = (0,2000)

main :: IO ()
main = do
  putStrLn "Plotting simulation results"
  putStrLn "- Calibration"
  plot_ants_calibrate
  putStrLn "- PSE"
  plot_ants_pse
  putStrLn "- Profiles"
  plot_ants_profiles
  putStrLn "Done."
