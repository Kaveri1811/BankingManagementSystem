import { useState } from "react";
import Navbar from "../components/Navbar";
import { transfer } from "../services/accountService";

function Transfer() {

    const [formData, setFormData] = useState({
        fromAccountNumber: localStorage.getItem("accountNumber"),
        toAccountNumber: "",
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

            const response = await transfer(formData);

            setMessage(response.message || "Transfer Successful");

            setFormData({
                ...formData,
                toAccountNumber: "",
                amount: "",
                description: ""
            });

        } catch (error) {

            if (error.response?.data?.message) {
                setMessage(error.response.data.message);
            } else {
                setMessage("Transfer Failed");
            }

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

                                <h3 className="text-center mb-4">
                                    Transfer Money
                                </h3>

                                {message &&
                                    <div className="alert alert-info">
                                        {message}
                                    </div>
                                }

                                <form onSubmit={handleSubmit}>

                                    <div className="mb-3">

                                        <label>From Account</label>

                                        <input
                                            className="form-control"
                                            value={formData.fromAccountNumber}
                                            readOnly
                                        />

                                    </div>

                                    <div className="mb-3">

                                        <label>To Account</label>

                                        <input
                                            type="text"
                                            className="form-control"
                                            name="toAccountNumber"
                                            value={formData.toAccountNumber}
                                            onChange={handleChange}
                                            required
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
                                            type="text"
                                            className="form-control"
                                            name="description"
                                            value={formData.description}
                                            onChange={handleChange}
                                        />

                                    </div>

                                    <button
                                        className="btn btn-primary w-100"
                                        type="submit">

                                        Transfer

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

export default Transfer;