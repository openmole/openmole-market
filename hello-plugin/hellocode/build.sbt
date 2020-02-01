name := "hellocode"

version := "1.0"

scalaVersion := "2.12.10"

enablePlugins(SbtOsgi)

OsgiKeys.exportPackage := Seq("hellocode.*")

OsgiKeys.importPackage := Seq("*")

OsgiKeys.privatePackage := Seq("")



