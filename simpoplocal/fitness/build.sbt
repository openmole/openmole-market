organization := "fr.geocite.simpoplocal"

name := "simpoplocal"

version := "1.0.0"

scalaVersion := "3.2.2"

libraryDependencies += "ca.umontreal.iro" % "ssj" % "2.5" excludeAll(
  ExclusionRule(organization = "dsol"), 
  ExclusionRule(organization = "jfree"), 
  ExclusionRule(organization = "org.apache.mahout"))

//resolvers += "ISC-PIF" at "http://maven.iscpif.fr/public"

enablePlugins(SbtOsgi)

osgiSettings

OsgiKeys.exportPackage := Seq("fr.geocites.simpoplocal.*")

OsgiKeys.importPackage := Seq("*;resolution:=optional")

OsgiKeys.privatePackage := Seq("!scala.*","*")
OsgiKeys.requireCapability := ""
