package com.lptemplatecompany.lptemplatedivision.lptemplateservicename
package config

/**
  * Top level application resources held in a Resource[...] so that proper cleanup happens
  * on program termination, whether clean or failure.
  */
final case class Context[F[_]] private(
)

object Context {

//  /** Managed resource lifetime with automatic cleanup */
//  def create: Resource[AIO, Context[AIO, IO, Message, Action]] =
//    for {
//      cfg <- Config.resource
//      log <- Resource.liftF(Slf4jLogger.create[AIO])
//      info <- Info.resource[AIO, Config](cfg, log)
//      mode <- AppMode.resource(cfg, log)
//      source <- mode.source(cfg, log)
//      sink <- mode.sink(cfg, log)
//    } yield new Context[AIO, IO, Message, Action](cfg, log, info, source, sink)

}
