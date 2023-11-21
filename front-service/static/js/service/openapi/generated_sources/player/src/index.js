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


import ApiClient from './ApiClient';
import JwtResponseOpenApi from './model/JwtResponseOpenApi';
import PlayerOpenApi from './model/PlayerOpenApi';
import PlayerApi from './api/PlayerApi';


/**
* JS API client generated by OpenAPI Generator.<br>
* The <code>index</code> module provides access to constructors for all the classes which comprise the public API.
* <p>
* An AMD (recommended!) or CommonJS application will generally do something equivalent to the following:
* <pre>
* var ApiTitle = require('index'); // See note below*.
* var xxxSvc = new ApiTitle.XxxApi(); // Allocate the API class we're going to use.
* var yyyModel = new ApiTitle.Yyy(); // Construct a model instance.
* yyyModel.someProperty = 'someValue';
* ...
* var zzz = xxxSvc.doSomething(yyyModel); // Invoke the service.
* ...
* </pre>
* <em>*NOTE: For a top-level AMD script, use require(['index'], function(){...})
* and put the application logic within the callback function.</em>
* </p>
* <p>
* A non-AMD browser application (discouraged) might do something like this:
* <pre>
* var xxxSvc = new ApiTitle.XxxApi(); // Allocate the API class we're going to use.
* var yyy = new ApiTitle.Yyy(); // Construct a model instance.
* yyyModel.someProperty = 'someValue';
* ...
* var zzz = xxxSvc.doSomething(yyyModel); // Invoke the service.
* ...
* </pre>
* </p>
* @module index
* @version 1.0
*/
export {
    /**
     * The ApiClient constructor.
     * @property {module:ApiClient}
     */
    ApiClient,

    /**
     * The JwtResponseOpenApi model constructor.
     * @property {module:model/JwtResponseOpenApi}
     */
    JwtResponseOpenApi,

    /**
     * The PlayerOpenApi model constructor.
     * @property {module:model/PlayerOpenApi}
     */
    PlayerOpenApi,

    /**
    * The PlayerApi service constructor.
    * @property {module:api/PlayerApi}
    */
    PlayerApi
};
