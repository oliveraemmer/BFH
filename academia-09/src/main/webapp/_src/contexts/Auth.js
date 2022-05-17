import { Router, useRouter } from "next/router";
import { createContext, useContext, useEffect, useState } from "react";
import Cookies from 'js-cookie'
import Swal from 'sweetalert2'

import Api from "../config/Api";

const AuthContext = createContext({});

export const AuthProvider = ({ children }) => {

    const [user, setUser] = useState(null);
    const [person, setPerson] = useState(null);
    const [loading, setLoading] = useState(true);
    const router = useRouter();

    useEffect(() => {
        async function loadUserFromCookie() {
            const token = Cookies.get('token');
            if (token) {
                try {
                    Api.defaults.headers.Authorization = 'Bearer ' + token;
                    const { data: user } = await Api.get('/auth');
                    if (user) {
                        setUser(user);
                    }
                    const { data: person } = await Api.get('/persons/' + user.pid);
                    if (person) {
                        setPerson(person);
                    }
                    setLoading(false);
                } catch (error) {
                    logout("noalert");
                    console.log(error);
                    router.push('/');
                }
            } else {
                logout("noalert");
                router.push('/');
            }
            setLoading(false);
        }
        loadUserFromCookie();
    }, []);

    const login = async (username, password) => {
        const { data: token } = await Api.post('/auth', {
            username,
            password,
        });
        if (token) {
            Api.defaults.headers.Authorization = 'Bearer ' + token;
            const { data: user } = await Api.get('/auth');
            if (user) {
                setUser(user);
            }
            const { data: person } = await Api.get('/persons/' + user.pid);
            if (person) {
                setPerson(person);
            }
            setLoading(false);
        }
        Cookies.set('token', token);
        Swal.fire({
            title: 'Logged in!',
            icon: 'success',
            toast: true,
            position: 'top',
            showConfirmButton: false,
            timer: 2000,
            timerProgressBar: true,
        });
    }

    const logout = (noalert) => {
        Cookies.remove('token');
        setUser(null);
        setPerson(null);
        delete Api.defaults.headers.Authorization;
        if (!noalert) {
            Swal.fire({
                title: 'Logged out!',
                icon: 'success',
                toast: true,
                position: 'top',
                showConfirmButton: false,
                timer: 2000,
                timerProgressBar: true,
            });
        }
    }

    return (
        <AuthContext.Provider value={{ isAuthenticated: !!user, user, login, loading, logout, person }}>
            {children}
        </AuthContext.Provider>
    );
};

export const useAuth = () => useContext(AuthContext);