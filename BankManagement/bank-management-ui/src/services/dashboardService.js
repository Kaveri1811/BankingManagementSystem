import api from "./api";

export const getDashboard = async (accountNumber) => {

    const response = await api.get(
        `/accounts/${accountNumber}/dashboard`
    );

    return response.data;
};