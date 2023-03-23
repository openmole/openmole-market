name := "hellocode"

version := "1.0"

scalaVersion := "3.2.2"

enablePlugins(SbtOsgi)

OsgiKeys.exportPackage := Seq("hellocode.*")

OsgiKeys.importPackage := Seq("*")

OsgiKeys.privatePackage := Seq("!scala.*,*")



