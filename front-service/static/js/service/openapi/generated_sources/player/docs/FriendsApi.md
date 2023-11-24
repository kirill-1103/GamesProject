# ApiTitle.FriendsApi

All URIs are relative to *http://localhost:8084/api/player*

Method | HTTP request | Description
------------- | ------------- | -------------
[**acceptFriendRequest**](FriendsApi.md#acceptFriendRequest) | **PUT** /friends/request/accept/{request_id} | accept friend request
[**getFriends**](FriendsApi.md#getFriends) | **GET** /friends | Get friends
[**getReceivedFriendRequest**](FriendsApi.md#getReceivedFriendRequest) | **GET** /friends/request/received/{player_id} | get received friend requests
[**getSentFriendRequest**](FriendsApi.md#getSentFriendRequest) | **GET** /friends/request/sent/{player_id} | get sent friend requests
[**rejectFriendRequest**](FriendsApi.md#rejectFriendRequest) | **PUT** /friends/request/reject/{request_id} | reject friend request
[**removeFriend**](FriendsApi.md#removeFriend) | **DELETE** /friends/{player_id}/{friend_id} | remove friend
[**revokeFriendRequest**](FriendsApi.md#revokeFriendRequest) | **DELETE** /friends/request/{request_id} | revoke friend request
[**sendFriendRequest**](FriendsApi.md#sendFriendRequest) | **POST** /friends/request | send friend request



## acceptFriendRequest

> acceptFriendRequest(requestId)

accept friend request

### Example

```javascript
import ApiTitle from 'api_title';

let apiInstance = new ApiTitle.FriendsApi();
let requestId = 789; // Number | 
apiInstance.acceptFriendRequest(requestId, (error, data, response) => {
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
 **requestId** | **Number**|  | 

### Return type

null (empty response body)

### Authorization

No authorization required

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: Not defined


## getFriends

> [PlayerOpenApi] getFriends(id, from, to)

Get friends

### Example

```javascript
import ApiTitle from 'api_title';

let apiInstance = new ApiTitle.FriendsApi();
let id = 789; // Number | 
let from = 789; // Number | 
let to = 789; // Number | 
apiInstance.getFriends(id, from, to, (error, data, response) => {
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
 **from** | **Number**|  | 
 **to** | **Number**|  | 

### Return type

[**[PlayerOpenApi]**](PlayerOpenApi.md)

### Authorization

No authorization required

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: application/json


## getReceivedFriendRequest

> [FriendRequestOpenApi] getReceivedFriendRequest(playerId)

get received friend requests

### Example

```javascript
import ApiTitle from 'api_title';

let apiInstance = new ApiTitle.FriendsApi();
let playerId = 789; // Number | 
apiInstance.getReceivedFriendRequest(playerId, (error, data, response) => {
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

[**[FriendRequestOpenApi]**](FriendRequestOpenApi.md)

### Authorization

No authorization required

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: application/json


## getSentFriendRequest

> [FriendRequestOpenApi] getSentFriendRequest(playerId)

get sent friend requests

### Example

```javascript
import ApiTitle from 'api_title';

let apiInstance = new ApiTitle.FriendsApi();
let playerId = 789; // Number | 
apiInstance.getSentFriendRequest(playerId, (error, data, response) => {
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

[**[FriendRequestOpenApi]**](FriendRequestOpenApi.md)

### Authorization

No authorization required

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: application/json


## rejectFriendRequest

> rejectFriendRequest(requestId)

reject friend request

### Example

```javascript
import ApiTitle from 'api_title';

let apiInstance = new ApiTitle.FriendsApi();
let requestId = 789; // Number | 
apiInstance.rejectFriendRequest(requestId, (error, data, response) => {
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
 **requestId** | **Number**|  | 

### Return type

null (empty response body)

### Authorization

No authorization required

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: Not defined


## removeFriend

> removeFriend(friendId, playerId)

remove friend

### Example

```javascript
import ApiTitle from 'api_title';

let apiInstance = new ApiTitle.FriendsApi();
let friendId = 789; // Number | 
let playerId = 789; // Number | 
apiInstance.removeFriend(friendId, playerId, (error, data, response) => {
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
 **friendId** | **Number**|  | 
 **playerId** | **Number**|  | 

### Return type

null (empty response body)

### Authorization

No authorization required

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: Not defined


## revokeFriendRequest

> revokeFriendRequest(requestId)

revoke friend request

### Example

```javascript
import ApiTitle from 'api_title';

let apiInstance = new ApiTitle.FriendsApi();
let requestId = 789; // Number | 
apiInstance.revokeFriendRequest(requestId, (error, data, response) => {
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
 **requestId** | **Number**|  | 

### Return type

null (empty response body)

### Authorization

No authorization required

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: Not defined


## sendFriendRequest

> sendFriendRequest(friendRequestOpenApi)

send friend request

### Example

```javascript
import ApiTitle from 'api_title';

let apiInstance = new ApiTitle.FriendsApi();
let friendRequestOpenApi = new ApiTitle.FriendRequestOpenApi(); // FriendRequestOpenApi | 
apiInstance.sendFriendRequest(friendRequestOpenApi, (error, data, response) => {
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
 **friendRequestOpenApi** | [**FriendRequestOpenApi**](FriendRequestOpenApi.md)|  | 

### Return type

null (empty response body)

### Authorization

No authorization required

### HTTP request headers

- **Content-Type**: application/json
- **Accept**: Not defined

