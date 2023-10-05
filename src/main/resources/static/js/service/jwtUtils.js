export function tokenIsExpired(token){
    const tokenParts = token.split('.');
    const encodedPayload = tokenParts[1];
    const decodedPayload = atob(encodedPayload);
    const payloadObj = JSON.parse(decodedPayload);
    if (!payloadObj.exp) {
        return false;

    }
    const tokenExpiration = payloadObj.exp * 1000;
    const currentTimeStamp = Date.now();
    return (currentTimeStamp >= tokenExpiration);
}