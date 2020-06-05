/**
 * Returns the endpoint URL (SmartHut backend URL)
 * @returns {String} endpoint URL
 */
export function endpointURL() {
  return window.BACKEND_URL !== '__BACKEND_URL__'
    ? window.BACKEND_URL
    : 'http://localhost:8080';
}

export function socketURL(token) {
  const httpURL = new URL(endpointURL());
  const isSecure = httpURL.protocol === 'https:';
  const protocol = isSecure ? 'wss:' : 'ws:';
  const port = httpURL.port || (isSecure ? 443 : 80);
  const url = `${protocol}//${httpURL.hostname}:${port}/sensor-socket?token=${token}`;
  console.log('socket url: ', url);
  return url;
}
