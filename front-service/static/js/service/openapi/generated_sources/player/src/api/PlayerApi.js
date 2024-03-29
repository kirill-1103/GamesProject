/**
 * API Title
 * No description provided (generated by Openapi Generator https://github.com/openapitools/openapi-generator)
 *
 * The version of the OpenAPI document: 1.0
 * 
 *
 * NOTE: This class is auto generated by OpenAPI Generator (https://openapi-generator.tech).
 * https://openapi-generator.tech
 * Do not edit the class manually.
 *
 */


import ApiClient from "../ApiClient";
import JwtResponseOpenApi from '../model/JwtResponseOpenApi';
import PlayerOpenApi from '../model/PlayerOpenApi';

/**
* Player service.
* @module api/PlayerApi
* @version 1.0
*/
export default class PlayerApi {

    /**
    * Constructs a new PlayerApi. 
    * @alias module:api/PlayerApi
    * @class
    * @param {module:ApiClient} [apiClient] Optional API client implementation to use,
    * default to {@link module:ApiClient#instance} if unspecified.
    */
    constructor(apiClient) {
        this.apiClient = apiClient || ApiClient.instance;
    }


    /**
     * Callback function to receive the result of the createPlayer operation.
     * @callback module:api/PlayerApi~createPlayerCallback
     * @param {String} error Error message, if any.
     * @param data This operation does not return a value.
     * @param {String} response The complete HTTP response.
     */

    /**
     * Create player
     * @param {module:model/PlayerOpenApi} player 
     * @param {Object} opts Optional parameters
     * @param {File} [img] Image's file
     * @param {module:api/PlayerApi~createPlayerCallback} callback The callback function, accepting three arguments: error, data, response
     */
    createPlayer(player, opts, callback) {
      opts = opts || {};
      let postBody = null;
      // verify the required parameter 'player' is set
      if (player === undefined || player === null) {
        throw new Error("Missing the required parameter 'player' when calling createPlayer");
      }

      let pathParams = {
      };
      let queryParams = {
      };
      let headerParams = {
      };
      let formParams = {
        'player': player,
        'img': opts['img']
      };

      let authNames = [];
      let contentTypes = ['multipart/form-data'];
      let accepts = [];
      let returnType = null;
      return this.apiClient.callApi(
        '/new', 'POST',
        pathParams, queryParams, headerParams, formParams, postBody,
        authNames, contentTypes, accepts, returnType, null, callback
      );
    }

    /**
     * Callback function to receive the result of the getActivePlayers operation.
     * @callback module:api/PlayerApi~getActivePlayersCallback
     * @param {String} error Error message, if any.
     * @param {Array.<module:model/PlayerOpenApi>} data The data returned by the service call.
     * @param {String} response The complete HTTP response.
     */

    /**
     * Get all online users
     * @param {module:api/PlayerApi~getActivePlayersCallback} callback The callback function, accepting three arguments: error, data, response
     * data is of type: {@link Array.<module:model/PlayerOpenApi>}
     */
    getActivePlayers(callback) {
      let postBody = null;

      let pathParams = {
      };
      let queryParams = {
      };
      let headerParams = {
      };
      let formParams = {
      };

      let authNames = [];
      let contentTypes = [];
      let accepts = ['application/json'];
      let returnType = [PlayerOpenApi];
      return this.apiClient.callApi(
        '/active', 'GET',
        pathParams, queryParams, headerParams, formParams, postBody,
        authNames, contentTypes, accepts, returnType, null, callback
      );
    }

    /**
     * Callback function to receive the result of the getAllOrderedByRatingStepByStep operation.
     * @callback module:api/PlayerApi~getAllOrderedByRatingStepByStepCallback
     * @param {String} error Error message, if any.
     * @param {Array.<module:model/PlayerOpenApi>} data The data returned by the service call.
     * @param {String} response The complete HTTP response.
     */

    /**
     * Get players sorting by rating step by step
     * @param {Number} from 
     * @param {Number} to 
     * @param {module:api/PlayerApi~getAllOrderedByRatingStepByStepCallback} callback The callback function, accepting three arguments: error, data, response
     * data is of type: {@link Array.<module:model/PlayerOpenApi>}
     */
    getAllOrderedByRatingStepByStep(from, to, callback) {
      let postBody = null;
      // verify the required parameter 'from' is set
      if (from === undefined || from === null) {
        throw new Error("Missing the required parameter 'from' when calling getAllOrderedByRatingStepByStep");
      }
      // verify the required parameter 'to' is set
      if (to === undefined || to === null) {
        throw new Error("Missing the required parameter 'to' when calling getAllOrderedByRatingStepByStep");
      }

      let pathParams = {
        'from': from,
        'to': to
      };
      let queryParams = {
      };
      let headerParams = {
      };
      let formParams = {
      };

      let authNames = [];
      let contentTypes = [];
      let accepts = ['application/json'];
      let returnType = [PlayerOpenApi];
      return this.apiClient.callApi(
        '/rating/{from}/{to}', 'GET',
        pathParams, queryParams, headerParams, formParams, postBody,
        authNames, contentTypes, accepts, returnType, null, callback
      );
    }

    /**
     * Callback function to receive the result of the getAuthenticatedUser operation.
     * @callback module:api/PlayerApi~getAuthenticatedUserCallback
     * @param {String} error Error message, if any.
     * @param {module:model/PlayerOpenApi} data The data returned by the service call.
     * @param {String} response The complete HTTP response.
     */

    /**
     * Current player info
     * @param {module:api/PlayerApi~getAuthenticatedUserCallback} callback The callback function, accepting three arguments: error, data, response
     * data is of type: {@link module:model/PlayerOpenApi}
     */
    getAuthenticatedUser(callback) {
      let postBody = null;

      let pathParams = {
      };
      let queryParams = {
      };
      let headerParams = {
      };
      let formParams = {
      };

      let authNames = ['bearerAuth'];
      let contentTypes = [];
      let accepts = ['application/json'];
      let returnType = PlayerOpenApi;
      return this.apiClient.callApi(
        '/authenticated', 'GET',
        pathParams, queryParams, headerParams, formParams, postBody,
        authNames, contentTypes, accepts, returnType, null, callback
      );
    }

    /**
     * Callback function to receive the result of the getById operation.
     * @callback module:api/PlayerApi~getByIdCallback
     * @param {String} error Error message, if any.
     * @param {module:model/PlayerOpenApi} data The data returned by the service call.
     * @param {String} response The complete HTTP response.
     */

    /**
     * Player Info
     * @param {Number} id 
     * @param {module:api/PlayerApi~getByIdCallback} callback The callback function, accepting three arguments: error, data, response
     * data is of type: {@link module:model/PlayerOpenApi}
     */
    getById(id, callback) {
      let postBody = null;
      // verify the required parameter 'id' is set
      if (id === undefined || id === null) {
        throw new Error("Missing the required parameter 'id' when calling getById");
      }

      let pathParams = {
        'id': id
      };
      let queryParams = {
      };
      let headerParams = {
      };
      let formParams = {
      };

      let authNames = ['bearerAuth'];
      let contentTypes = [];
      let accepts = ['application/json'];
      let returnType = PlayerOpenApi;
      return this.apiClient.callApi(
        '/{id}', 'GET',
        pathParams, queryParams, headerParams, formParams, postBody,
        authNames, contentTypes, accepts, returnType, null, callback
      );
    }

    /**
     * Callback function to receive the result of the getByLogin operation.
     * @callback module:api/PlayerApi~getByLoginCallback
     * @param {String} error Error message, if any.
     * @param {module:model/PlayerOpenApi} data The data returned by the service call.
     * @param {String} response The complete HTTP response.
     */

    /**
     * Player Info
     * @param {String} login 
     * @param {module:api/PlayerApi~getByLoginCallback} callback The callback function, accepting three arguments: error, data, response
     * data is of type: {@link module:model/PlayerOpenApi}
     */
    getByLogin(login, callback) {
      let postBody = null;
      // verify the required parameter 'login' is set
      if (login === undefined || login === null) {
        throw new Error("Missing the required parameter 'login' when calling getByLogin");
      }

      let pathParams = {
        'login': login
      };
      let queryParams = {
      };
      let headerParams = {
      };
      let formParams = {
      };

      let authNames = ['bearerAuth'];
      let contentTypes = [];
      let accepts = ['application/json'];
      let returnType = PlayerOpenApi;
      return this.apiClient.callApi(
        '/login/{login}', 'GET',
        pathParams, queryParams, headerParams, formParams, postBody,
        authNames, contentTypes, accepts, returnType, null, callback
      );
    }

    /**
     * Callback function to receive the result of the getCurrentGameCode operation.
     * @callback module:api/PlayerApi~getCurrentGameCodeCallback
     * @param {String} error Error message, if any.
     * @param {Number} data The data returned by the service call.
     * @param {String} response The complete HTTP response.
     */

    /**
     * Get current game code
     * @param {Number} playerId 
     * @param {module:api/PlayerApi~getCurrentGameCodeCallback} callback The callback function, accepting three arguments: error, data, response
     * data is of type: {@link Number}
     */
    getCurrentGameCode(playerId, callback) {
      let postBody = null;
      // verify the required parameter 'playerId' is set
      if (playerId === undefined || playerId === null) {
        throw new Error("Missing the required parameter 'playerId' when calling getCurrentGameCode");
      }

      let pathParams = {
        'player_id': playerId
      };
      let queryParams = {
      };
      let headerParams = {
      };
      let formParams = {
      };

      let authNames = [];
      let contentTypes = [];
      let accepts = ['application/json'];
      let returnType = 'Number';
      return this.apiClient.callApi(
        '/currentGameCode/{player_id}', 'GET',
        pathParams, queryParams, headerParams, formParams, postBody,
        authNames, contentTypes, accepts, returnType, null, callback
      );
    }

    /**
     * Callback function to receive the result of the getCurrentGameId operation.
     * @callback module:api/PlayerApi~getCurrentGameIdCallback
     * @param {String} error Error message, if any.
     * @param {Number} data The data returned by the service call.
     * @param {String} response The complete HTTP response.
     */

    /**
     * Get current game id
     * @param {Number} playerId 
     * @param {module:api/PlayerApi~getCurrentGameIdCallback} callback The callback function, accepting three arguments: error, data, response
     * data is of type: {@link Number}
     */
    getCurrentGameId(playerId, callback) {
      let postBody = null;
      // verify the required parameter 'playerId' is set
      if (playerId === undefined || playerId === null) {
        throw new Error("Missing the required parameter 'playerId' when calling getCurrentGameId");
      }

      let pathParams = {
        'player_id': playerId
      };
      let queryParams = {
      };
      let headerParams = {
      };
      let formParams = {
      };

      let authNames = [];
      let contentTypes = [];
      let accepts = ['application/json'];
      let returnType = 'Number';
      return this.apiClient.callApi(
        '/currentGameId/{player_id}', 'GET',
        pathParams, queryParams, headerParams, formParams, postBody,
        authNames, contentTypes, accepts, returnType, null, callback
      );
    }

    /**
     * Callback function to receive the result of the getPlayerTop operation.
     * @callback module:api/PlayerApi~getPlayerTopCallback
     * @param {String} error Error message, if any.
     * @param {Number} data The data returned by the service call.
     * @param {String} response The complete HTTP response.
     */

    /**
     * Get player position in top by player id
     * @param {Number} id 
     * @param {module:api/PlayerApi~getPlayerTopCallback} callback The callback function, accepting three arguments: error, data, response
     * data is of type: {@link Number}
     */
    getPlayerTop(id, callback) {
      let postBody = null;
      // verify the required parameter 'id' is set
      if (id === undefined || id === null) {
        throw new Error("Missing the required parameter 'id' when calling getPlayerTop");
      }

      let pathParams = {
        'id': id
      };
      let queryParams = {
      };
      let headerParams = {
      };
      let formParams = {
      };

      let authNames = [];
      let contentTypes = [];
      let accepts = ['application/json'];
      let returnType = 'Number';
      return this.apiClient.callApi(
        '/top/{id}', 'GET',
        pathParams, queryParams, headerParams, formParams, postBody,
        authNames, contentTypes, accepts, returnType, null, callback
      );
    }

    /**
     * Callback function to receive the result of the getPlayersByIds operation.
     * @callback module:api/PlayerApi~getPlayersByIdsCallback
     * @param {String} error Error message, if any.
     * @param {Object.<String, module:model/{String: PlayerOpenApi}>} data The data returned by the service call.
     * @param {String} response The complete HTTP response.
     */

    /**
     * get players by ids
     * @param {Array.<Number>} requestBody 
     * @param {module:api/PlayerApi~getPlayersByIdsCallback} callback The callback function, accepting three arguments: error, data, response
     * data is of type: {@link Object.<String, module:model/{String: PlayerOpenApi}>}
     */
    getPlayersByIds(requestBody, callback) {
      let postBody = requestBody;
      // verify the required parameter 'requestBody' is set
      if (requestBody === undefined || requestBody === null) {
        throw new Error("Missing the required parameter 'requestBody' when calling getPlayersByIds");
      }

      let pathParams = {
      };
      let queryParams = {
      };
      let headerParams = {
      };
      let formParams = {
      };

      let authNames = [];
      let contentTypes = ['application/json'];
      let accepts = ['application/json'];
      let returnType = {'String': PlayerOpenApi};
      return this.apiClient.callApi(
        '/ids', 'POST',
        pathParams, queryParams, headerParams, formParams, postBody,
        authNames, contentTypes, accepts, returnType, null, callback
      );
    }

    /**
     * Callback function to receive the result of the savePlayer operation.
     * @callback module:api/PlayerApi~savePlayerCallback
     * @param {String} error Error message, if any.
     * @param {module:model/PlayerOpenApi} data The data returned by the service call.
     * @param {String} response The complete HTTP response.
     */

    /**
     * Save player
     * @param {module:model/PlayerOpenApi} playerOpenApi 
     * @param {module:api/PlayerApi~savePlayerCallback} callback The callback function, accepting three arguments: error, data, response
     * data is of type: {@link module:model/PlayerOpenApi}
     */
    savePlayer(playerOpenApi, callback) {
      let postBody = playerOpenApi;
      // verify the required parameter 'playerOpenApi' is set
      if (playerOpenApi === undefined || playerOpenApi === null) {
        throw new Error("Missing the required parameter 'playerOpenApi' when calling savePlayer");
      }

      let pathParams = {
      };
      let queryParams = {
      };
      let headerParams = {
      };
      let formParams = {
      };

      let authNames = ['bearerAuth'];
      let contentTypes = ['application/json'];
      let accepts = ['application/json'];
      let returnType = PlayerOpenApi;
      return this.apiClient.callApi(
        '/save', 'POST',
        pathParams, queryParams, headerParams, formParams, postBody,
        authNames, contentTypes, accepts, returnType, null, callback
      );
    }

    /**
     * Callback function to receive the result of the searchPlayers operation.
     * @callback module:api/PlayerApi~searchPlayersCallback
     * @param {String} error Error message, if any.
     * @param {Array.<module:model/PlayerOpenApi>} data The data returned by the service call.
     * @param {String} response The complete HTTP response.
     */

    /**
     * Search players
     * @param {String} searchQuery 
     * @param {Number} from 
     * @param {Number} to 
     * @param {module:api/PlayerApi~searchPlayersCallback} callback The callback function, accepting three arguments: error, data, response
     * data is of type: {@link Array.<module:model/PlayerOpenApi>}
     */
    searchPlayers(searchQuery, from, to, callback) {
      let postBody = null;
      // verify the required parameter 'searchQuery' is set
      if (searchQuery === undefined || searchQuery === null) {
        throw new Error("Missing the required parameter 'searchQuery' when calling searchPlayers");
      }
      // verify the required parameter 'from' is set
      if (from === undefined || from === null) {
        throw new Error("Missing the required parameter 'from' when calling searchPlayers");
      }
      // verify the required parameter 'to' is set
      if (to === undefined || to === null) {
        throw new Error("Missing the required parameter 'to' when calling searchPlayers");
      }

      let pathParams = {
      };
      let queryParams = {
        'search_query': searchQuery,
        'from': from,
        'to': to
      };
      let headerParams = {
      };
      let formParams = {
      };

      let authNames = [];
      let contentTypes = [];
      let accepts = ['application/json'];
      let returnType = [PlayerOpenApi];
      return this.apiClient.callApi(
        '/search', 'GET',
        pathParams, queryParams, headerParams, formParams, postBody,
        authNames, contentTypes, accepts, returnType, null, callback
      );
    }

    /**
     * Callback function to receive the result of the updateActive operation.
     * @callback module:api/PlayerApi~updateActiveCallback
     * @param {String} error Error message, if any.
     * @param data This operation does not return a value.
     * @param {String} response The complete HTTP response.
     */

    /**
     * Update active
     * @param {module:api/PlayerApi~updateActiveCallback} callback The callback function, accepting three arguments: error, data, response
     */
    updateActive(callback) {
      let postBody = null;

      let pathParams = {
      };
      let queryParams = {
      };
      let headerParams = {
      };
      let formParams = {
      };

      let authNames = ['bearerAuth'];
      let contentTypes = [];
      let accepts = [];
      let returnType = null;
      return this.apiClient.callApi(
        '/update-active', 'POST',
        pathParams, queryParams, headerParams, formParams, postBody,
        authNames, contentTypes, accepts, returnType, null, callback
      );
    }

    /**
     * Callback function to receive the result of the updatePlayer operation.
     * @callback module:api/PlayerApi~updatePlayerCallback
     * @param {String} error Error message, if any.
     * @param {module:model/JwtResponseOpenApi} data The data returned by the service call.
     * @param {String} response The complete HTTP response.
     */

    /**
     * Update player
     * @param {String} login 
     * @param {String} email 
     * @param {Number} id 
     * @param {Object} opts Optional parameters
     * @param {String} [password] 
     * @param {File} [img] Image's file
     * @param {module:api/PlayerApi~updatePlayerCallback} callback The callback function, accepting three arguments: error, data, response
     * data is of type: {@link module:model/JwtResponseOpenApi}
     */
    updatePlayer(login, email, id, opts, callback) {
      opts = opts || {};
      let postBody = null;
      // verify the required parameter 'login' is set
      if (login === undefined || login === null) {
        throw new Error("Missing the required parameter 'login' when calling updatePlayer");
      }
      // verify the required parameter 'email' is set
      if (email === undefined || email === null) {
        throw new Error("Missing the required parameter 'email' when calling updatePlayer");
      }
      // verify the required parameter 'id' is set
      if (id === undefined || id === null) {
        throw new Error("Missing the required parameter 'id' when calling updatePlayer");
      }

      let pathParams = {
      };
      let queryParams = {
      };
      let headerParams = {
      };
      let formParams = {
        'login': login,
        'email': email,
        'id': id,
        'password': opts['password'],
        'img': opts['img']
      };

      let authNames = ['bearerAuth'];
      let contentTypes = ['multipart/form-data'];
      let accepts = ['application/json'];
      let returnType = JwtResponseOpenApi;
      return this.apiClient.callApi(
        '/update', 'POST',
        pathParams, queryParams, headerParams, formParams, postBody,
        authNames, contentTypes, accepts, returnType, null, callback
      );
    }


}
