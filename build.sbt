organization        := "io.github.gantu"
name                := "scala-post-office"
version             := "0.0.1-SNAPSHOT"
scalaVersion        := "2.12.8"

resolvers += Resolver.sonatypeRepo("snapshots")

val CatsVersion            = "1.6.0"
val CirceVersion           = "0.9.3"
val CirceConfigVersion     = "0.6.1"
val DoobieVersion          = "0.5.3"
val EnumeratumCirceVersion = "1.5.21"
val H2Version              = "1.4.199"
val http4sVersion          = "0.18.4"
val KindProjectorVersion   = "0.9.9"
val LogbackVersion         = "1.2.3"
val ScalaCheckVersion      = "1.14.0"
val ScalaTestVersion       = "3.0.7"
val FlywayVersion          = "5.2.4"
val TsecVersion            = "0.1.0"
val pureConfigVersion = "0.9.1"

libraryDependencies ++= Seq(
  "org.typelevel"         %% "cats-core"              % CatsVersion,
  "io.circe"              %% "circe-generic"          % CirceVersion,
  "io.circe"              %% "circe-literal"          % CirceVersion,
  "io.circe"              %% "circe-generic-extras"   % CirceVersion,
  "io.circe"              %% "circe-parser"           % CirceVersion,
  "io.circe"              %% "circe-config"           % CirceConfigVersion,
  "org.tpolecat"          %% "doobie-core"            % DoobieVersion,
  "org.tpolecat"          %% "doobie-h2"              % DoobieVersion,
  "org.tpolecat"          %% "doobie-scalatest"       % DoobieVersion,
  "org.tpolecat"          %% "doobie-hikari"          % DoobieVersion,
  "com.beachape"          %% "enumeratum-circe"       % EnumeratumCirceVersion,
  "com.h2database"        %  "h2"                     % H2Version,

  "com.github.pureconfig" %% "pureconfig" % pureConfigVersion,

  "org.http4s"            %% "http4s-blaze-server"     % http4sVersion,
  "org.http4s"            %% "http4s-circe"            % http4sVersion,
  "org.http4s"            %% "http4s-dsl"              % http4sVersion,
  "org.http4s"            %% "http4s-blaze-client"            % http4sVersion,

  "ch.qos.logback"        %  "logback-classic"        % LogbackVersion,
  "org.flywaydb"          %  "flyway-core"            % FlywayVersion,
  "org.scalacheck"        %% "scalacheck"             % ScalaCheckVersion % Test,
  "org.scalatest"         %% "scalatest"              % ScalaTestVersion  % Test,
)
