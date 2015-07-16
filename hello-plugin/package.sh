
cd hellocode
sbt clean osgi-bundle
cp target/scala-2.11/hellocode_*.jar ../plugins/
cd -

cd hellotask
sbt clean osgi-bundle
cp target/scala-2.11/hellotask_*.jar ../plugins/
cd -
