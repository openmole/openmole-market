name := "hellocode"

version := "1.0"

scalaVersion := "2.11.7"

osgiSettings

OsgiKeys.exportPackage := Seq("hellocode.*")

OsgiKeys.importPackage := Seq("*")

OsgiKeys.privatePackage := Seq("")

scalariformSettings


