import Axios from 'axios';
import Cookies from 'js-cookie';

let urls = {
    test: 'http://localhost:8080/api',
    development: 'http://localhost:8080/api', // local development
    production: 'http://localhost:8080/api',
}

let Api = Axios.create({
    baseURL: urls[process.env.NODE_ENV],
    headers: {
        'Accept': 'application/json',
        'Content-Type': 'application/json'
    }
});

const token = Cookies.get('token');
if(token) {
    Api.defaults.headers.common['Authorization'] = `Bearer ${token}`;
}

export default Api;