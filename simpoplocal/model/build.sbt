organization := "fr.geocite"

name := "simpoplocal"

version := "1.0.0"

scalaVersion := "2.11.7"

libraryDependencies += "ca.umontreal.iro" % "ssj" % "2.5" excludeAll(
  ExclusionRule(organization = "dsol"),
  ExclusionRule(organization = "jfree"),
  ExclusionRule(organization = "org.apache.mahout"))

osgiSettings

OsgiKeys.exportPackage := Seq("fr.geocite.simpoplocal.*")

OsgiKeys.importPackage := Seq("*;resolution:=optional")

OsgiKeys.privatePackage := Seq("!scala.*,*")

