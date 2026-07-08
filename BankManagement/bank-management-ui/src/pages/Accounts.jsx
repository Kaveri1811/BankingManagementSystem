import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import { getAccounts } from "../services/accountService";
import Navbar from "../components/Navbar";

function Accounts() {

    const [accounts, setAccounts] = useState([]);

    const navigate = useNavigate();

    useEffect(() => {

        loadAccounts();

    }, []);

    const loadAccounts = async () => {

        try {

            const data = await getAccounts();

            setAccounts(data);

        } catch (error) {

            console.log(error);

        }

    };

    const openDashboard = (accountNumber) => {

        localStorage.setItem("accountNumber", accountNumber);

        navigate("/dashboard");

    };

    return (

        <>
            <Navbar />

            <div className="container mt-4">

                <div className="d-flex justify-content-between align-items-center mb-4">

                    <h2>My Accounts</h2>

                    <button
                        className="btn btn-success"
                        onClick={() => navigate("/create-account")}
                    >
                        + Create Account
                    </button>

                </div>

                {accounts.length === 0 ? (

                    <div className="text-center mt-5">

                        <h4>No Accounts Found</h4>

                        <p>Create your first bank account.</p>

                        <button
                            className="btn btn-primary"
                            onClick={() => navigate("/create-account")}
                        >
                            Create Account
                        </button>

                    </div>

                ) : (

                    <table className="table table-bordered table-hover">

                        <thead className="table-dark">

                            <tr>

                                <th>Account Number</th>
                                <th>Account Type</th>
                                <th>Balance</th>
                                <th>Status</th>
                                <th>Action</th>

                            </tr>

                        </thead>

                        <tbody>

                            {accounts.map(account => (

                                <tr key={account.accountNumber}>

                                    <td>{account.accountNumber}</td>

                                    <td>{account.accountType}</td>

                                    <td>₹ {account.balance}</td>

                                    <td>{account.status}</td>

                                    <td>

                                        <button
                                            className="btn btn-primary btn-sm"
                                            onClick={() =>
                                                openDashboard(account.accountNumber)
                                            }
                                        >
                                            Open Dashboard
                                        </button>

                                    </td>

                                </tr>

                            ))}

                        </tbody>

                    </table>

                )}

            </div>

        </>

    );

}

export default Accounts;