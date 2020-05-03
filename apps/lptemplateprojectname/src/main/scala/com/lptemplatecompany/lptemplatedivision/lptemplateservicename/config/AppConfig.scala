package com.lptemplatecompany.lptemplatedivision.lptemplateservicename
package config

import cats.Monad
import cats.effect.IO
import cats.syntax.contravariantSemigroupal._
import cats.syntax.either._
import cats.syntax.functor._
import com.leighperry.conduction.config.{ Configured, Conversion, Environment }
import io.chrisdavenport.log4cats.Logger
import com.leighperry.conduction.config.magnolia.AutoConfigInstancesIO._


/**
 * Overall application configuration
 */
final case class AppConfig(
  kafka: KafkaConfig
)

// Magnolia-generated config
object AppConfig {

  private val baseName = "LPTEMPLATESERVICENAME_LPTEMPLATEENVPREFIX"
  private val sep = "\n    "

  def load(log: Logger[IO]): IO[AppConfig] =
    for {
      _ <- log.info(s"All config parameters:${sep}${Configured[IO, AppConfig].description(baseName).prettyPrint(sep)}")
      env <- Environment.fromEnvVars[IO]
      logenv = Environment.logging[IO](env, Environment.printer[IO])
      cio <- Configured[IO, AppConfig](baseName).run(logenv)
      result <- IO.fromEither(cio.toEither.leftMap(AppError.InvalidConfiguration))
    } yield result

  val defaults: AppConfig =
    AppConfig(
      kafka = KafkaConfig(
        bootstrapServers = KafkaBootstrapServers("localhost:9092"),
        schemaRegistryUrl = KafkaSchemaRegistryUrl("http://localhost:8081"),
        List.empty,
        None
      )
    )

}

case class KafkaConfig(
  bootstrapServers: KafkaBootstrapServers,
  schemaRegistryUrl: KafkaSchemaRegistryUrl,
  properties: List[PropertyValue],
  verbose: Option[Boolean]
)

// Explicit config for newtypes
final case class KafkaBootstrapServers(value: String) extends AnyVal
object KafkaBootstrapServers {
  implicit def conversion: Conversion[KafkaBootstrapServers] =
    Conversion[String].map(KafkaBootstrapServers.apply)
}

final case class KafkaSchemaRegistryUrl(value: String) extends AnyVal
object KafkaSchemaRegistryUrl {
  implicit def conversion: Conversion[KafkaSchemaRegistryUrl] =
    Conversion[String].map(KafkaSchemaRegistryUrl.apply)
}

case class PropertyValue(name: String, value: String)
