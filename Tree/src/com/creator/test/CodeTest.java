package com.creator.test;

import static org.junit.Assert.*;

import org.junit.Test;

import com.creator.api.CodeEnum;

public class CodeTest {

	@Test
	public void testCode() {
		CodeEnum code = CodeEnum.SUCCESS;
		System.out.println(code == CodeEnum.SUCCESS);
	}

}
