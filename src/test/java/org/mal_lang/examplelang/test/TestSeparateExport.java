package org.mal_lang.examplelang.test;

import core.*;
import core.coverage.*;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;

public class TestSeparateExport extends ExampleLangTest {

	private static Host machine;
	private static User user;
	private static Password password;

	// disable encryption by default
	public static boolean isEncryptedEnabledDefault = false;

	@BeforeAll
	public static void setup() {
		machine = new Host("PC");
		user = new User("User");
		password = new Password("qwerty", isEncryptedEnabledDefault);
		
		machine.addPasswords(password);
		password.addUser(user);
	}

	@AfterAll
	public static void clear() {
		password.encrypted.defaultValue = isEncryptedEnabledDefault;
	}

	@Test
	public void testDefDisable() {
		password.encrypted.defaultValue = false;
		Attacker attacker = new Attacker();

		attacker.addAttackPoint(machine.connect);
		attacker.attack();
	}

	@Test
	public void testDefEnabled() {
		password.encrypted.defaultValue = true;
		Attacker attacker = new Attacker();

		attacker.addAttackPoint(machine.connect);
		attacker.attack();
	}
}