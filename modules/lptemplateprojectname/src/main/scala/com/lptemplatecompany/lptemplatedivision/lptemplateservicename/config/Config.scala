package com.lptemplatecompany.lptemplatedivision.lptemplateservicename
package config

import cats.Monad
import cats.effect.{IO, Resource}
import cats.syntax.contravariantSemigroupal._
import cats.syntax.either._
import cats.syntax.functor._
import com.leighperry.conduction.config.{Configured, Conversion, Environment}


/**
  * Overall application configuration
  */
final case class Config(
  kafka: KafkaConfig,
)

object Config {

  implicit def configured[F[_]](implicit F: Monad[F]): Configured[F, Config] =
    Configured[F, KafkaConfig].withSuffix("KAFKA")
      .map(Config.apply)

  def load: IO[Config] =
    for {
      env <- Environment.fromEnvVars[IO]
      logenv <- Environment.logging[IO](env, Environment.printer)
      cio <- Configured[IO, Config]("LPTEMPLATESERVICENAME").run(logenv)
      result <- IO.fromEither(cio.toEither.leftMap(AppError.InvalidConfiguration))
    } yield result

  def resource: Resource[IO, Config] =
    Resource.liftF(load)

  val defaults: Config =
    Config(
      kafka =
        KafkaConfig(
          bootstrapServers = KafkaBootstrapServers("localhost:9092"),
          List.empty,
          None,
        ),
    )

}

case class KafkaConfig(
  bootstrapServers: KafkaBootstrapServers,
  properties: List[PropertyValue],
  verbose: Option[Boolean]
)

object KafkaConfig {
  implicit def configured[F[_]](implicit F: Monad[F]): Configured[F, KafkaConfig] = (
    Configured[F, KafkaBootstrapServers].withSuffix("BOOTSTRAP_SERVERS"),
    Configured[F, List[PropertyValue]].withSuffix("PROPERTY"),
    Configured[F, Option[Boolean]].withSuffix("VERBOSE"),
  ).mapN(KafkaConfig.apply)
}

final case class KafkaBootstrapServers(value: String) extends AnyVal
object KafkaBootstrapServers {
  implicit def conversion: Conversion[KafkaBootstrapServers] =
    Conversion[String].map(KafkaBootstrapServers.apply)
}

case class PropertyValue(name: String, value: String)

object PropertyValue {
  implicit def configured[F[_]](implicit F: Monad[F]): Configured[F, PropertyValue] = (
    Configured[F, String].withSuffix("NAME"),
    Configured[F, String].withSuffix("VALUE"),
  ).mapN(PropertyValue.apply)
}
