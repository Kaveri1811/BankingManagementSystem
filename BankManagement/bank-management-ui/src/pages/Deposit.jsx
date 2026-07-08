import { useState } from "react";
import Navbar from "../components/Navbar";
import { deposit } from "../services/accountService";

function Deposit() {

    const [formData, setFormData] = useState({
        accountNumber: localStorage.getItem("accountNumber"),
        amount: "",
        description: ""
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

            const response = await deposit(formData);

            setMessage(response.message || "Deposit Successful");

            setFormData({
                ...formData,
                amount: "",
                description: ""
            });

        } catch (error) {

            setMessage("Deposit Failed");

        }

    };

    return (

        <>
            <Navbar />

            <div className="container mt-4">

                <div className="row justify-content-center">

                    <div className="col-md-6">

                        <div className="card shadow">

                            <div className="card-body">

                                <h3 className="mb-4 text-center">
                                    Deposit Money
                                </h3>

                                {message &&
                                    <div className="alert alert-info">
                                        {message}
                                    </div>
                                }

                                <form onSubmit={handleSubmit}>

                                    <div className="mb-3">

                                        <label>Account Number</label>

                                        <input
                                            className="form-control"
                                            value={formData.accountNumber}
                                            readOnly
                                        />

                                    </div>

                                    <div className="mb-3">

                                        <label>Amount</label>

                                        <input
                                            type="number"
                                            className="form-control"
                                            name="amount"
                                            value={formData.amount}
                                            onChange={handleChange}
                                            required
                                        />

                                    </div>

                                    <div className="mb-3">

                                        <label>Description</label>

                                        <input
                                            className="form-control"
                                            name="description"
                                            value={formData.description}
                                            onChange={handleChange}
                                        />

                                    </div>

                                    <button
                                        className="btn btn-success w-100">

                                        Deposit

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

export default Deposit;