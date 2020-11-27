name := "hellocode"

version := "1.0"

scalaVersion := "2.13.4"

enablePlugins(SbtOsgi)

OsgiKeys.exportPackage := Seq("hellocode.*")

OsgiKeys.importPackage := Seq("*")

OsgiKeys.privatePackage := Seq("!scala.*,*")



