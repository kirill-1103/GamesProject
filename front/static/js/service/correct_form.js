export function checkLogin(login){
    let regForLogin = /^[a-z]+(\d|[a-z])*$/
    console.log(!login || !(login.length === 0 || login.length>70 || !regForLogin.test(login)));
    return login !== null && !(login.length === 0 || login.length>70 || !regForLogin.test(login));
}

export  function checkPassword(password){
    let regForPassword = /^(\d|[a-z])+$/
    return password !== null && !(password.length === 0 || password.length>70 || !regForPassword.test(password))
}

export function checkPasswordEmptyOk(password){
    let regForPassword = /^(\d|[a-z])+$/
    return !password || !(password.length>70 || !regForPassword.test(password))
}

export  function checkEmail(email){
    let regForEmail = /^(([^<>()[\].,;:\s@"]+(\.[^<>()[\].,;:\s@"]+)*)|(".+"))@(([^<>()[\].,;:\s@"]+\.)+[^<>()[\].,;:\s@"]{2,})$/iu
    return email !== null && !(email.length === 0 || email.length>90 || !regForEmail.test(email));
}