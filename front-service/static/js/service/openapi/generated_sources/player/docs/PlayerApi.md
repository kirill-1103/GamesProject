# ApiTitle.PlayerApi

All URIs are relative to *http://localhost:8084/api/player*

Method | HTTP request | Description
------------- | ------------- | -------------
[**createPlayer**](PlayerApi.md#createPlayer) | **POST** /new | Create player
[**getActivePlayers**](PlayerApi.md#getActivePlayers) | **GET** /active | Get all online users
[**getAllOrderedByRatingStepByStep**](PlayerApi.md#getAllOrderedByRatingStepByStep) | **GET** /rating/{from}/{to} | Get players sorting by rating step by step
[**getAuthenticatedUser**](PlayerApi.md#getAuthenticatedUser) | **GET** /authenticated | Current player info
[**getById**](PlayerApi.md#getById) | **GET** /{id} | Player Info
[**getByLogin**](PlayerApi.md#getByLogin) | **GET** /login/{login} | Player Info
[**getCurrentGameCode**](PlayerApi.md#getCurrentGameCode) | **GET** /currentGameCode/{player_id} | Get current game code
[**getCurrentGameId**](PlayerApi.md#getCurrentGameId) | **GET** /currentGameId/{player_id} | Get current game id
[**getPlayerTop**](PlayerApi.md#getPlayerTop) | **GET** /top/{id} | Get player position in top by player id
[**getPlayersByIds**](PlayerApi.md#getPlayersByIds) | **POST** /ids | get players by ids
[**savePlayer**](PlayerApi.md#savePlayer) | **POST** /save | Save player
[**searchPlayers**](PlayerApi.md#searchPlayers) | **GET** /search | Search players
[**updateActive**](PlayerApi.md#updateActive) | **POST** /update-active | Update active
[**updatePlayer**](PlayerApi.md#updatePlayer) | **POST** /update | Update player



## createPlayer

> createPlayer(player, opts)

Create player

### Example

```javascript
import ApiTitle from 'api_title';

let apiInstance = new ApiTitle.PlayerApi();
let player = new ApiTitle.PlayerOpenApi(); // PlayerOpenApi | 
let opts = {
  'img': "/path/to/file" // File | Image's file
};
apiInstance.createPlayer(player, opts, (error, data, response) => {
  if (error) {
    console.error(error);
  } else {
    console.log('API called successfully.');
  }
});
```

### Parameters


Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **player** | [**PlayerOpenApi**](PlayerOpenApi.md)|  | 
 **img** | **File**| Image&#39;s file | [optional] 

### Return type

null (empty response body)

### Authorization

No authorization required

### HTTP request headers

- **Content-Type**: multipart/form-data
- **Accept**: Not defined


## getActivePlayers

> [PlayerOpenApi] getActivePlayers()

Get all online users

### Example

```javascript
import ApiTitle from 'api_title';

let apiInstance = new ApiTitle.PlayerApi();
apiInstance.getActivePlayers((error, data, response) => {
  if (error) {
    console.error(error);
  } else {
    console.log('API called successfully. Returned data: ' + data);
  }
});
```

### Parameters

This endpoint does not need any parameter.

### Return type

[**[PlayerOpenApi]**](PlayerOpenApi.md)

### Authorization

No authorization required

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: application/json


## getAllOrderedByRatingStepByStep

> [PlayerOpenApi] getAllOrderedByRatingStepByStep(from, to)

Get players sorting by rating step by step

### Example

```javascript
import ApiTitle from 'api_title';

let apiInstance = new ApiTitle.PlayerApi();
let from = 789; // Number | 
let to = 789; // Number | 
apiInstance.getAllOrderedByRatingStepByStep(from, to, (error, data, response) => {
  if (error) {
    console.error(error);
  } else {
    console.log('API called successfully. Returned data: ' + data);
  }
});
```

### Parameters


Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **from** | **Number**|  | 
 **to** | **Number**|  | 

### Return type

[**[PlayerOpenApi]**](PlayerOpenApi.md)

### Authorization

No authorization required

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: application/json


## getAuthenticatedUser

> PlayerOpenApi getAuthenticatedUser()

Current player info

### Example

```javascript
import ApiTitle from 'api_title';
let defaultClient = ApiTitle.ApiClient.instance;
// Configure Bearer (JWT) access token for authorization: bearerAuth
let bearerAuth = defaultClient.authentications['bearerAuth'];
bearerAuth.accessToken = "YOUR ACCESS TOKEN"

let apiInstance = new ApiTitle.PlayerApi();
apiInstance.getAuthenticatedUser((error, data, response) => {
  if (error) {
    console.error(error);
  } else {
    console.log('API called successfully. Returned data: ' + data);
  }
});
```

### Parameters

This endpoint does not need any parameter.

### Return type

[**PlayerOpenApi**](PlayerOpenApi.md)

### Authorization

[bearerAuth](../README.md#bearerAuth)

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: application/json


## getById

> PlayerOpenApi getById(id)

Player Info

### Example

```javascript
import ApiTitle from 'api_title';
let defaultClient = ApiTitle.ApiClient.instance;
// Configure Bearer (JWT) access token for authorization: bearerAuth
let bearerAuth = defaultClient.authentications['bearerAuth'];
bearerAuth.accessToken = "YOUR ACCESS TOKEN"

let apiInstance = new ApiTitle.PlayerApi();
let id = 789; // Number | 
apiInstance.getById(id, (error, data, response) => {
  if (error) {
    console.error(error);
  } else {
    console.log('API called successfully. Returned data: ' + data);
  }
});
```

### Parameters


Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **id** | **Number**|  | 

### Return type

[**PlayerOpenApi**](PlayerOpenApi.md)

### Authorization

[bearerAuth](../README.md#bearerAuth)

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: application/json


## getByLogin

> PlayerOpenApi getByLogin(login)

Player Info

### Example

```javascript
import ApiTitle from 'api_title';
let defaultClient = ApiTitle.ApiClient.instance;
// Configure Bearer (JWT) access token for authorization: bearerAuth
let bearerAuth = defaultClient.authentications['bearerAuth'];
bearerAuth.accessToken = "YOUR ACCESS TOKEN"

let apiInstance = new ApiTitle.PlayerApi();
let login = "login_example"; // String | 
apiInstance.getByLogin(login, (error, data, response) => {
  if (error) {
    console.error(error);
  } else {
    console.log('API called successfully. Returned data: ' + data);
  }
});
```

### Parameters


Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **login** | **String**|  | 

### Return type

[**PlayerOpenApi**](PlayerOpenApi.md)

### Authorization

[bearerAuth](../README.md#bearerAuth)

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: application/json


## getCurrentGameCode

> Number getCurrentGameCode(playerId)

Get current game code

### Example

```javascript
import ApiTitle from 'api_title';

let apiInstance = new ApiTitle.PlayerApi();
let playerId = 789; // Number | 
apiInstance.getCurrentGameCode(playerId, (error, data, response) => {
  if (error) {
    console.error(error);
  } else {
    console.log('API called successfully. Returned data: ' + data);
  }
});
```

### Parameters


Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **playerId** | **Number**|  | 

### Return type

**Number**

### Authorization

No authorization required

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: application/json


## getCurrentGameId

> Number getCurrentGameId(playerId)

Get current game id

### Example

```javascript
import ApiTitle from 'api_title';

let apiInstance = new ApiTitle.PlayerApi();
let playerId = 789; // Number | 
apiInstance.getCurrentGameId(playerId, (error, data, response) => {
  if (error) {
    console.error(error);
  } else {
    console.log('API called successfully. Returned data: ' + data);
  }
});
```

### Parameters


Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **playerId** | **Number**|  | 

### Return type

**Number**

### Authorization

No authorization required

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: application/json


## getPlayerTop

> Number getPlayerTop(id)

Get player position in top by player id

### Example

```javascript
import ApiTitle from 'api_title';

let apiInstance = new ApiTitle.PlayerApi();
let id = 789; // Number | 
apiInstance.getPlayerTop(id, (error, data, response) => {
  if (error) {
    console.error(error);
  } else {
    console.log('API called successfully. Returned data: ' + data);
  }
});
```

### Parameters


Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **id** | **Number**|  | 

### Return type

**Number**

### Authorization

No authorization required

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: application/json


## getPlayersByIds

> {String: PlayerOpenApi} getPlayersByIds(requestBody)

get players by ids

### Example

```javascript
import ApiTitle from 'api_title';

let apiInstance = new ApiTitle.PlayerApi();
let requestBody = [null]; // [Number] | 
apiInstance.getPlayersByIds(requestBody, (error, data, response) => {
  if (error) {
    console.error(error);
  } else {
    console.log('API called successfully. Returned data: ' + data);
  }
});
```

### Parameters


Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **requestBody** | [**[Number]**](Number.md)|  | 

### Return type

[**{String: PlayerOpenApi}**](PlayerOpenApi.md)

### Authorization

No authorization required

### HTTP request headers

- **Content-Type**: application/json
- **Accept**: application/json


## savePlayer

> PlayerOpenApi savePlayer(playerOpenApi)

Save player

### Example

```javascript
import ApiTitle from 'api_title';
let defaultClient = ApiTitle.ApiClient.instance;
// Configure Bearer (JWT) access token for authorization: bearerAuth
let bearerAuth = defaultClient.authentications['bearerAuth'];
bearerAuth.accessToken = "YOUR ACCESS TOKEN"

let apiInstance = new ApiTitle.PlayerApi();
let playerOpenApi = new ApiTitle.PlayerOpenApi(); // PlayerOpenApi | 
apiInstance.savePlayer(playerOpenApi, (error, data, response) => {
  if (error) {
    console.error(error);
  } else {
    console.log('API called successfully. Returned data: ' + data);
  }
});
```

### Parameters


Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **playerOpenApi** | [**PlayerOpenApi**](PlayerOpenApi.md)|  | 

### Return type

[**PlayerOpenApi**](PlayerOpenApi.md)

### Authorization

[bearerAuth](../README.md#bearerAuth)

### HTTP request headers

- **Content-Type**: application/json
- **Accept**: application/json


## searchPlayers

> [PlayerOpenApi] searchPlayers(searchQuery, from, to)

Search players

### Example

```javascript
import ApiTitle from 'api_title';

let apiInstance = new ApiTitle.PlayerApi();
let searchQuery = "searchQuery_example"; // String | 
let from = 789; // Number | 
let to = 789; // Number | 
apiInstance.searchPlayers(searchQuery, from, to, (error, data, response) => {
  if (error) {
    console.error(error);
  } else {
    console.log('API called successfully. Returned data: ' + data);
  }
});
```

### Parameters


Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **searchQuery** | **String**|  | 
 **from** | **Number**|  | 
 **to** | **Number**|  | 

### Return type

[**[PlayerOpenApi]**](PlayerOpenApi.md)

### Authorization

No authorization required

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: application/json


## updateActive

> updateActive()

Update active

### Example

```javascript
import ApiTitle from 'api_title';
let defaultClient = ApiTitle.ApiClient.instance;
// Configure Bearer (JWT) access token for authorization: bearerAuth
let bearerAuth = defaultClient.authentications['bearerAuth'];
bearerAuth.accessToken = "YOUR ACCESS TOKEN"

let apiInstance = new ApiTitle.PlayerApi();
apiInstance.updateActive((error, data, response) => {
  if (error) {
    console.error(error);
  } else {
    console.log('API called successfully.');
  }
});
```

### Parameters

This endpoint does not need any parameter.

### Return type

null (empty response body)

### Authorization

[bearerAuth](../README.md#bearerAuth)

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: Not defined


## updatePlayer

> JwtResponseOpenApi updatePlayer(login, email, id, opts)

Update player

### Example

```javascript
import ApiTitle from 'api_title';
let defaultClient = ApiTitle.ApiClient.instance;
// Configure Bearer (JWT) access token for authorization: bearerAuth
let bearerAuth = defaultClient.authentications['bearerAuth'];
bearerAuth.accessToken = "YOUR ACCESS TOKEN"

let apiInstance = new ApiTitle.PlayerApi();
let login = "login_example"; // String | 
let email = "email_example"; // String | 
let id = 789; // Number | 
let opts = {
  'password': "password_example", // String | 
  'img': "/path/to/file" // File | Image's file
};
apiInstance.updatePlayer(login, email, id, opts, (error, data, response) => {
  if (error) {
    console.error(error);
  } else {
    console.log('API called successfully. Returned data: ' + data);
  }
});
```

### Parameters


Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **login** | **String**|  | 
 **email** | **String**|  | 
 **id** | **Number**|  | 
 **password** | **String**|  | [optional] 
 **img** | **File**| Image&#39;s file | [optional] 

### Return type

[**JwtResponseOpenApi**](JwtResponseOpenApi.md)

### Authorization

[bearerAuth](../README.md#bearerAuth)

### HTTP request headers

- **Content-Type**: multipart/form-data
- **Accept**: application/json

