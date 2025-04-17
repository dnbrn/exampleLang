/*
 * Copyright 2020-2022 Foreseeti AB <https://foreseeti.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.mal_lang.examplelang.test;

import core.*;
import core.coverage.*;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;

public class TestGetPassword extends ExampleLangTest {

  private static Network internet;
  private static Host server;
  private static Password password;

  // disable encryption by default
  public static boolean isEncryptedEnabledDefault = false;

  @BeforeAll
  public static void setup() {
    internet = new Network("internet");
    server = new Host("server");
    password = new Password("password", isEncryptedEnabledDefault);

    internet.addHosts(server);
    server.addPasswords(password);
  }

  @AfterAll
  public static void clear() {
    password.encrypted.defaultValue = isEncryptedEnabledDefault;
  }

  @Test
  public void testGetPasswordQuantumNotEncrypted() {
    password.encrypted.defaultValue = false;
    Attacker attacker = new Attacker();

    attacker.addAttackPoint(internet.access);
    attacker.addAttackPoint(password.quantum);
    attacker.attack();

    server.access.assertCompromisedInstantaneously(); // TODO necessary?
  }

  @Test
  public void testGetPasswordQuantumEncrypted() {
    password.encrypted.defaultValue = true;
    Attacker attacker = new Attacker();

    attacker.addAttackPoint(internet.access);
    attacker.addAttackPoint(password.quantum);
    attacker.attack();

    server.access.assertCompromisedInstantaneously(); // TODO necessary?
  }

  @Test
  public void testGetPasswordNonQuantumNotEncrypted() {
    password.encrypted.defaultValue = false;
    Attacker attacker = new Attacker();

    attacker.addAttackPoint(internet.access);
    attacker.attack();

    server.access.assertCompromisedInstantaneously(); // TODO necessary?
  }

  @Test
  public void testGetPasswordNonQuantumEncrypted() {
    password.encrypted.defaultValue = true ;
    Attacker attacker = new Attacker();

    attacker.addAttackPoint(internet.access);
    attacker.attack();

    server.access.assertUncompromised(); // TODO necessary?
  }
}
