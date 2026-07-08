import api from "./api";

export const getAccounts = async () => {
    const response = await api.get("/accounts");
    return response.data;
};

export const deposit = async (request) => {
    const response = await api.post("/accounts/deposit", request);
    return response.data;
};

export const withdraw = async (request) => {

    const response = await api.post("/accounts/withdraw", request);

    return response.data;

};

export const transfer = async (request) => {

    const response = await api.post("/accounts/transfer", request);

    return response.data;

};

export const getTransactions = async (
    accountNumber,
    page,
    size,
    type
) => {

    let url =
        `/accounts/${accountNumber}/transactions?page=${page}&size=${size}`;

    if (type !== "") {

        url += `&type=${type}`;

    }

    const response = await api.get(url);

    return response.data;

};

export const getMiniStatement = async (
    accountNumber
) => {

    const response =
        await api.get(
            `/accounts/${accountNumber}/mini-statement`
        );

    return response.data;

};

export const downloadStatement = async (
    accountNumber
) => {

    const response =
        await api.get(
            `/accounts/${accountNumber}/statement/pdf`,
            {
                responseType: "blob"
            }
        );

    const url =
        window.URL.createObjectURL(
            new Blob([response.data])
        );

    const link =
        document.createElement("a");

    link.href = url;

    link.setAttribute(
        "download",
        "statement.pdf"
    );

    document.body.appendChild(link);

    link.click();

    link.remove();

};

export const createAccount = async (request) => {

    const response = await api.post("/accounts", request);

    return response.data;

};