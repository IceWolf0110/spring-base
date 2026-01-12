import axiosClient from "@/lib/axios-client.ts";
import router from "@/router";

export default {
    login: async (
        username: string,
        password: string
    ): Promise<void> => {
        await axiosClient.post('/auth/login', {
            username: username,
            password: password,
        }).then(res => {
           console.log(res)
        }).catch(err => {
            console.log(err.response.data)
        });
    },

    register: async (
        username: string,
        email: string,
        password: string
    ): Promise<void> => {
        await axiosClient.post('/register', {
            username: username,
            email: email,
            password: password,
        }).then(res => {
            console.log(res);
            router.push({ name: 'login' });
        });
    }
}