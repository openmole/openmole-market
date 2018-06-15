
resultFile <- "/tmp/sensitivity.png"

print("processing R with inputs")
print(inputnames)
print(outputnames)
print(mustars)
print(sigmas)

inputs <- unique(inputnames)

layoutCountColumns <- min(4,length(inputs))
layoutCountRows <- ceiling(length(inputs)/layoutCountColumns)

png(filename=resultFile, width=1280/4*layoutCountColumns, height=1280*layoutCountRows/layoutCountColumns*1.15, units="px", bg="white", type="cairo-png")

par(mfrow=c(layoutCountRows,layoutCountColumns), pty="s", cex=0.9) # pin=c(3.0,3.0/length(inputs)), 


minx <- min(mustars)
maxx <- max(mustars)

miny <- min(sigmas)
maxy <- max(sigmas)

marginy <- (maxy - miny)/20.0
marginy <- 0

offsetlabels <- (maxx - minx)/30.0
marginlabelright <- (maxx - minx)/100*max(sapply(inputnames,function(t) nchar(as.character(t))))


for (i in 1:length(inputs)) {


	chw <- par("cxy")[1]
	chh <- par("cxy")[2]  ##  character height
	
	plot(
		mustars[inputnames==inputs[i]], 
		sigmas[inputnames==inputs[i]], 
		xlab="mu*", ylab="sigma", 
		pch=21, lwd=3,
		col="blue", 
		main=paste("impact of",inputs[i]),
		xlim=c(0, maxx+marginlabelright),
		ylim=c(0, maxy+marginy)
		)

	grid()
	
	text(
		mustars[inputnames==inputs[i]]+offsetlabels, 
		sigmas[inputnames==inputs[i]], 
		labels=as.character(outputnames[inputnames==inputs[i]]),
		adj=0
		)

}

# close the plot and file
dev.off()

