package com.lptemplatecompany.lptemplatedivision.lptemplateservicename
package config

import cats.Monad
import cats.effect.IO
import cats.syntax.contravariantSemigroupal._
import cats.syntax.either._
import cats.syntax.functor._
import com.leighperry.conduction.config.{ Configured, Conversion, Environment }
import io.chrisdavenport.log4cats.Logger

/**
 * Overall application configuration
 */
final case class Config(
  kafka: KafkaConfig
)

object Config {

  implicit def configured[F[_]](implicit F: Monad[F]): Configured[F, Config] =
    Configured[F, KafkaConfig]
      .withSuffix("LPTEMPLATEENVPREFIX_KAFKA")
      .map(Config.apply)

  private val baseName = "LPTEMPLATESERVICENAME"
  private val sep = "\n    "

  def load(log: Logger[IO]): IO[Config] =
    for {
      _ <- log.info(s"All config parameters:${sep}${Configured[IO, Config].description(baseName).prettyPrint(sep)}")
      env <- Environment.fromEnvVars[IO]
      logenv = Environment.logging[IO](env, Environment.printer[IO])
      cio <- Configured[IO, Config](baseName).run(logenv)
      result <- IO.fromEither(cio.toEither.leftMap(AppError.InvalidConfiguration))
    } yield result

  val defaults: Config =
    Config(
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

object KafkaConfig {
  implicit def configured[F[_]](implicit F: Monad[F]): Configured[F, KafkaConfig] =
    (
      Configured[F, KafkaBootstrapServers].withSuffix("BOOTSTRAP_SERVERS"),
      Configured[F, KafkaSchemaRegistryUrl].withSuffix("SCHEMA_REGISTRY_URL"),
      Configured[F, List[PropertyValue]].withSuffix("PROPERTY"),
      Configured[F, Option[Boolean]].withSuffix("VERBOSE")
    ).mapN(KafkaConfig.apply)
}

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

object PropertyValue {
  implicit def configured[F[_]](implicit F: Monad[F]): Configured[F, PropertyValue] =
    (
      Configured[F, String].withSuffix("NAME"),
      Configured[F, String].withSuffix("VALUE")
    ).mapN(PropertyValue.apply)
}
