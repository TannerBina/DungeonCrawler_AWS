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
	
	//init and build create game service
	private final static CreateGameService createGameService =
			LambdaInvokerFactory.builder().lambdaClient(AWSLambdaClientBuilder
					.defaultClient()).build(CreateGameService.class);
	
	//init and build delete game service
	private final static DeleteGameService deleteGameService = 
			LambdaInvokerFactory.builder().lambdaClient(AWSLambdaClientBuilder
					.defaultClient()).build(DeleteGameService.class);
	
	//init and build fetch all games service
		private final static FetchAllGamesService fetchAllGamesService = 
				LambdaInvokerFactory.builder().lambdaClient(AWSLambdaClientBuilder
						.defaultClient()).build(FetchAllGamesService.class);
	
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
	
	/*
	 * Calls the create_game_function
	 */
	public static Output createGame(CreateGameInput in) {
		return createGameService.createGame(in);
	}
	
	/*
	 * Calls the delete_game_function
	 */
	public static Output deleteGame(DeleteGameInput in) {
		return deleteGameService.deleteGame(in);
	}
	
	/*
	 * Calls the fetch_all_games_function
	 */
	public static Output fetchAllGames(FetchAllGamesInput in) {
		return fetchAllGamesService.fetchAllGames(in);
	}
}
