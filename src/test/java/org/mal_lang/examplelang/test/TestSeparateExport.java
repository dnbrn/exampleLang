package org.mal_lang.examplelang.test;

import core.*;
//import mal.*;
import core.coverage.*;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;

import org.junit.jupiter.api.extension.RegisterExtension;
import org.junit.jupiter.api.extension.ExtendWith;

/* *
 * TestSeparateExport
 */
@ExtendWith(CoverageExtension.class)
public class TestSeparateExport {
	@RegisterExtension
	public CoverageExtension cove = new CoverageExtension(new JSONTarget());

	@RegisterExtension
	public CoverageExtension cove2 = new CoverageExtension(new ConsoleTarget());

	private static Host machine;
	private static User user;
	private static Password password;

	// enable encryption
	public static boolean isEncryptedEnabled = false;

	@BeforeAll
	public static void setup() {
		machine = new Host("PC");
		user = new User("User");
		password = new Password("qwerty", isEncryptedEnabled);
		
		machine.addPasswords(password);
		password.addUser(user);
	}

	@BeforeEach
	public void clearMdl() {
		new Attacker().reset();
	}

	/*
	method from ExampleLangTest.java
	potentially useful

	@AfterEach
	public void deleteModel() {
		Asset.allAssets.clear();
		AttackStep.allAttackSteps.clear();
		Defense.allDefenses.clear();
	}
	 */

	@AfterAll
	public static void clear() {
		password.encrypted.defaultValue = false;
	}

	@Test
	public void testDefDisable() {
		password.encrypted.defaultValue = false;
		Attacker a = new Attacker();
		a.addAttackPoint(machine.connect);
		a.attack();
	}

	@Test
	public void testDefEnabled() {
		password.encrypted.defaultValue = true;
		Attacker a = new Attacker();
		a.addAttackPoint(machine.connect);
		a.attack();
	}
}