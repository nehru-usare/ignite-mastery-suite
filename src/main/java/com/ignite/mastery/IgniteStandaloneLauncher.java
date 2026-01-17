package com.ignite.mastery;

import org.apache.ignite.Ignition;

/**
 * Ignite Standalone Server Launcher
 * 
 * Purpose: This class simulates a standalone Apache Ignite Server.
 * In a real production environment, you would run this on a separate
 * VM/Container.
 * 
 * Instructions:
 * 1. Run this class first to start the "Central Ignite Service".
 * 2. Then run the IgniteMasteryApplication (Spring Boot) to connect to it.
 */
public class IgniteStandaloneLauncher {
    public static void main(String[] args) {
        System.out.println("🚀 Starting Standalone Apache Ignite Server...");

        // Start Ignite using the external XML configuration
        Ignition.start("ignite-server.xml");

        System.out.println("✅ Standalone Ignite Server is UP and RUNNING!");
    }
}
