package com.amazonaws.util;

import com.amazonaws.lambdafunction.callers.*;
import com.amazonaws.services.lambda.AWSLambdaClientBuilder;
import com.amazonaws.services.lambda.invoke.LambdaInvokerFactory;

/*
 * Class for calling all lambda services
 */
public class LambdaServices {
	
	//init the login service and build it.
	private final static LoginService loginService = LambdaInvokerFactory.builder()
			.lambdaClient(AWSLambdaClientBuilder.defaultClient())
			.build(LoginService.class);
	
	//init and build fetch char service
	private final static FetchCharacterService fetchCharacterService =
			LambdaInvokerFactory.builder().lambdaClient(AWSLambdaClientBuilder
					.defaultClient()).build(FetchCharacterService.class);
	
	//init and build create user service
	private final static CreateUserService createUserService =
			LambdaInvokerFactory.builder().lambdaClient(AWSLambdaClientBuilder
					.defaultClient()).build(CreateUserService.class);
	
	//init and build delete character service
	private final static DeleteCharacterService deleteCharacterService =
			LambdaInvokerFactory.builder().lambdaClient(AWSLambdaClientBuilder
					.defaultClient()).build(DeleteCharacterService.class);
	
	/*
	 * Calls the login_function
	 */
	public static Output login(LoginInput in) {
		return loginService.login(in);
	}
	
	/*
	 * Calls the fetch_character_function
	 */
	public static Output fetchCharacter(FetchCharacterInput in) {
		return fetchCharacterService.fetchCharacter(in);
	}
	
	/*
	 * Calls the create_user_function
	 */
	public static Output createUser(CreateUserInput in) {
		return createUserService.createUser(in);
	}
	
	/*
	 * Calls the delete_character_function
	 */
	public static Output deleteCharacter(DeleteCharacterInput in) {
		return deleteCharacterService.deleteCharacter(in);
	}
}
