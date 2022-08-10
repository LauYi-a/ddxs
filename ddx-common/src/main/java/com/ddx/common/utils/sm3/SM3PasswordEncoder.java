package com.ddx.common.utils.sm3;

import org.springframework.security.crypto.password.PasswordEncoder;

public class SM3PasswordEncoder implements PasswordEncoder{

	@Override
	public String encode(CharSequence arg0) {
		return SM3Digest.decode(arg0.toString());
	}

	@Override
	public boolean matches(CharSequence arg0, String arg1) {
		return arg1.equals(SM3Digest.decode(arg0.toString()));
	}

}
