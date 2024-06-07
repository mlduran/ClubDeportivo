/* 
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Other/javascript.js to edit this template
 */


const urlParams = new URLSearchParams(window.location.search);
let code = urlParams.get('code');

const getToken = async code => {

  // stored in the previous step
  let codeVerifier = localStorage.getItem('code_verifier');

  const payload = {
    method: 'POST',
    headers: {
      'Content-Type': 'application/x-www-form-urlencoded'
    },
    body: new URLSearchParams({
      client_id: clientId,
      grant_type: 'authorization_code',
      code: code,
      redirect_uri: redirectUri,
      code_verifier: codeVerifier
    })
  };

  const body = await fetch(URL, payload);
  const response =await body.json();

  localStorage.setItem('access_token', response.access_token);
  
  
  async function getProfile(accessToken) {
  let accessToken = localStorage.getItem('access_token');

  const response = await fetch('https://api.spotify.com/v1/playlists/3cEYpjA9oz9GiPac4AsH4n', {
    headers: {
      Authorization: 'Bearer ' + accessToken
    }
  });

  const data = await response.json();
};

  
};
