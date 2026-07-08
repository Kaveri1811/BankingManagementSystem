import { useState } from "react";
import { useNavigate } from "react-router-dom";
import Navbar from "../components/Navbar";
import { createAccount } from "../services/accountService";

function CreateAccount() {

    const navigate = useNavigate();

    const [formData, setFormData] = useState({
        accountType: "SAVINGS",
        initialDeposit: ""
    });

    const [message, setMessage] = useState("");

    const handleChange = (e) => {

        setFormData({
            ...formData,
            [e.target.name]: e.target.value
        });

    };

    const handleSubmit = async (e) => {

        e.preventDefault();

        try {

            const response = await createAccount(formData);

            setMessage("Account Created Successfully!");

            // Optional: save newly created account number
            localStorage.setItem(
                "accountNumber",
                response.accountNumber
            );

            setTimeout(() => {

                navigate("/accounts");

            }, 1500);

        } catch (error) {

            if (error.response?.data?.message) {

                setMessage(error.response.data.message);

            } else {

                setMessage("Failed to create account.");

            }

        }

    };

    return (

        <>
            <Navbar />

            <div className="container mt-5">

                <div className="row justify-content-center">

                    <div className="col-md-6">

                        <div className="card shadow">

                            <div className="card-body">

                                <h2 className="text-center mb-4">

                                    Create New Account

                                </h2>

                                {message &&
                                    <div className="alert alert-info">

                                        {message}

                                    </div>
                                }

                                <form onSubmit={handleSubmit}>

                                    <div className="mb-3">

                                        <label className="form-label">

                                            Account Type

                                        </label>

                                        <select
                                            className="form-select"
                                            name="accountType"
                                            value={formData.accountType}
                                            onChange={handleChange}
                                        >

                                            <option value="SAVINGS">
                                                Savings
                                            </option>

                                            <option value="CURRENT">
                                                Current
                                            </option>

                                        </select>

                                    </div>

                                    <div className="mb-3">

                                        <label className="form-label">

                                            Initial Deposit

                                        </label>

                                        <input
                                            type="number"
                                            className="form-control"
                                            name="initialDeposit"
                                            value={formData.initialDeposit}
                                            onChange={handleChange}
                                            required
                                        />

                                    </div>

                                    <button
                                        className="btn btn-success w-100"
                                        type="submit">

                                        Create Account

                                    </button>

                                </form>

                            </div>

                        </div>

                    </div>

                </div>

            </div>

        </>

    );

}

export default CreateAccount;