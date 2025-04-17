/*
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

public class TestAll extends ExampleLangAbstractTest {

  private static Network internet;
  private static Host server;
  private static Host pc;
  private static User alice;
  private static Password passwordServerAlice;
  private static Password passwordPCAlice;

  // disable encryption by default
  public static boolean isEncryptedEnabledDefault = false;

  @BeforeAll
  public static void setup() {
    internet = new Network("internet");
    server = new Host("server");
    pc = new Host("PC");
    alice = new User("Alice");
    passwordServerAlice = new Password("passwordServerAlice", isEncryptedEnabledDefault);
    passwordPCAlice = new Password("passwordPCAlice", isEncryptedEnabledDefault);

    internet.addHosts(server);
    server.addPasswords(passwordServerAlice);
    alice.addPasswords(passwordServerAlice);
    pc.addPasswords(passwordPCAlice);
    passwordPCAlice.addUser(alice);
  }

  @AfterAll
  public static void clear() {
    passwordServerAlice.encrypted.defaultValue = isEncryptedEnabledDefault;
    passwordPCAlice.encrypted.defaultValue = isEncryptedEnabledDefault;
  }

  @Test
  public void testPhishingPasswordNotEncrypted() {
    passwordServerAlice.encrypted.defaultValue = false;
    Attacker attacker = new Attacker();

    attacker.addAttackPoint(internet.access);
    attacker.addAttackPoint(alice.attemptPhishing);
    attacker.attack();

    server.access.assertCompromisedInstantaneously();
  }

  @Test
  public void testPhishingPasswordEncrypted() {
    passwordServerAlice.encrypted.defaultValue = true;
    Attacker attacker = new Attacker();

    attacker.addAttackPoint(internet.access);
    attacker.addAttackPoint(alice.attemptPhishing);
    attacker.attack();

    server.access.assertCompromisedWithEffort();
  }

  @Test
  public void testAccessingPCPasswordNotEncrypted() {
    passwordPCAlice.encrypted.defaultValue = false;
    Attacker attacker = new Attacker();

    attacker.addAttackPoint(pc.connect);
    attacker.attack();

    pc.access.assertCompromisedInstantaneously();
  }

  @Test
  public void testAccessingPCPasswordEncrypted() {
    passwordPCAlice.encrypted.defaultValue = true;
    Attacker attacker = new Attacker();

    attacker.addAttackPoint(pc.connect);
    attacker.attack();

    pc.access.assertUncompromised();
  }

  @Test
  public void testAccessWithPasswordAccessNotEncrypted() {
    passwordServerAlice.encrypted.defaultValue = false;
    Attacker attacker = new Attacker();

    attacker.addAttackPoint(internet.access);
    attacker.addAttackPoint(passwordServerAlice.obtain);
    attacker.attack();

    server.access.assertCompromisedInstantaneously();
  }

  @Test
  public void testAccessWithoutPasswordAccessEncrypted() {
    passwordServerAlice.encrypted.defaultValue = true;
    Attacker attacker = new Attacker();

    attacker.addAttackPoint(internet.access);
    attacker.attack();

    server.access.assertUncompromised();
  }

  @Test
  public void testAccessWithoutPasswordAccessNotEncrypted() {
    passwordServerAlice.encrypted.defaultValue = false;
    Attacker attacker = new Attacker();

    attacker.addAttackPoint(internet.access);
    attacker.attack();

    server.access.assertCompromisedInstantaneously();
  }

  @Test
  public void testWithPasswordAccessNoNetworkNotEncrypted() {
    passwordServerAlice.encrypted.defaultValue = false;
    Attacker attacker = new Attacker();

    attacker.addAttackPoint(passwordServerAlice.obtain);
    attacker.attack();

    server.access.assertUncompromised();
  }

  @Test
  public void testGetPasswordQuantumNotEncrypted() {
    passwordServerAlice.encrypted.defaultValue = false;
    Attacker attacker = new Attacker();

    attacker.addAttackPoint(internet.access);
    attacker.addAttackPoint(passwordServerAlice.quantum);
    attacker.attack();

    server.access.assertCompromisedInstantaneously();
  }

  @Test
  public void testGetPasswordQuantumEncrypted() {
    passwordServerAlice.encrypted.defaultValue = true;
    Attacker attacker = new Attacker();

    attacker.addAttackPoint(internet.access);
    attacker.addAttackPoint(passwordServerAlice.quantum);
    attacker.attack();

    server.access.assertCompromisedInstantaneously();
  }

  @Test
  public void testGetPasswordNonQuantumNotEncrypted() {
    passwordServerAlice.encrypted.defaultValue = false;
    Attacker attacker = new Attacker();

    attacker.addAttackPoint(internet.access);
    attacker.attack();

    server.access.assertCompromisedInstantaneously();
  }

  @Test
  public void testGetPasswordNonQuantumEncrypted() {
    passwordServerAlice.encrypted.defaultValue = true ;
    Attacker attacker = new Attacker();

    attacker.addAttackPoint(internet.access);
    attacker.attack();

    server.access.assertUncompromised();
  }
}
