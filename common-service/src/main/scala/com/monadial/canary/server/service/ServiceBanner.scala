package com.monadial.canary.server.service

import scala.io.AnsiColor._
import com.monadial.canary.server.service.model.InstanceName

import java.net.InetAddress

case class ServiceBanner(instanceName: InstanceName) {

  lazy val inetAddress: InetAddress = java.net.InetAddress.getLocalHost

  private val systemNameBanner: String =
    """
      |   ____
      |  / ___|__ _ _ __   __ _ _ __ _   _
      | | |   / _` | '_ \ / _` | '__| | | |
      | | |__| (_| | | | | (_| | |  | |_| |
      |  \____\__,_|_| |_|\__,_|_|   \__, |
      | __   __                      |___/     _ _        _                   _
      | \ \ / /__  _   _ _ __     __| (_) __ _(_) |_ __ _| |   ___  ___ _ __ | |_ _ __ _   _
      |  \ V / _ \| | | | '__|   / _` | |/ _` | | __/ _` | |  / __|/ _ \ '_ \| __| '__| | | |
      |   | | (_) | |_| | |     | (_| | | (_| | | || (_| | |  \__ \  __/ | | | |_| |  | |_| |
      |   |_|\___/ \__,_|_|      \__,_|_|\__, |_|\__\__,_|_|  |___/\___|_| |_|\__|_|   \__, |
      |                                  |___/                                         |___/
      |""".stripMargin

  val banner: String =
    List(
      "\n",
      systemNameBanner,
      "\n",
      s"""
        | ${GREEN}
        | service name: ${instanceName.serviceName.name}
        | service uuid: ${instanceName.instanceUuid.toString}
        | service hostname: ${inetAddress.getHostName}
        | service ip: ${inetAddress.getHostAddress}
        | cluster name: ${instanceName.clusterName.name}
        | member name: ${instanceName.toString}
        | start time: ${instanceName.startTime.asDate().toString}
        | ${RESET}
        |""".stripMargin,
    ).foldRight("")(_.concat(_))
}
