import { useEffect, useState } from "react";
import Navbar from "../components/Navbar";
import {
    getTransactions,
    getMiniStatement,
    downloadStatement
} from "../services/accountService";

function Transactions() {

    const accountNumber = localStorage.getItem("accountNumber");

    const [transactions, setTransactions] = useState([]);
    const [miniStatement, setMiniStatement] = useState([]);

    const [page, setPage] = useState(0);
    const [size] = useState(5);

    const [totalPages, setTotalPages] = useState(0);

    const [type, setType] = useState("");

    useEffect(() => {

        loadTransactions();
        loadMiniStatement();

    }, [page, type]);

    const loadTransactions = async () => {

        try {

            const data = await getTransactions(
                accountNumber,
                page,
                size,
                type
            );

            setTransactions(data.content);
            setTotalPages(data.totalPages);

        } catch (error) {

            console.log(error);

        }

    };

    const loadMiniStatement = async () => {

        try {

            const data =
                await getMiniStatement(accountNumber);

            setMiniStatement(data);

        } catch (error) {

            console.log(error);

        }

    };

    const handleDownload = async () => {

        await downloadStatement(accountNumber);

    };

    return (

        <>
            <Navbar />

            <div className="container mt-4">

                <h2>Transactions</h2>

                <hr />

                <div className="row mb-3">

                    <div className="col-md-3">

                        <select
                            className="form-select"
                            value={type}
                            onChange={(e) => {

                                setType(e.target.value);
                                setPage(0);

                            }}
                        >

                            <option value="">
                                All Transactions
                            </option>

                            <option value="DEPOSIT">
                                Deposit
                            </option>

                            <option value="WITHDRAWAL">
                                Withdrawal
                            </option>

                            <option value="TRANSFER">
                                Transfer
                            </option>

                        </select>

                    </div>

                </div>

                <h4>Transaction History</h4>

                <table className="table table-bordered table-striped">

                    <thead>

                        <tr>

                            <th>Type</th>
                            <th>Amount</th>
                            <th>Description</th>
                            <th>Date</th>

                        </tr>

                    </thead>

                    <tbody>

                        {transactions.map(transaction => (

                            <tr
                                key={transaction.transactionId}
                            >

                                <td>
                                    {transaction.transactionType}
                                </td>

                                <td>
                                    ₹ {transaction.amount}
                                </td>

                                <td>
                                    {transaction.description}
                                </td>

                                <td>
                                    {transaction.transactionDate}
                                </td>

                            </tr>

                        ))}

                    </tbody>

                </table>

                <div className="d-flex justify-content-between">

                    <button
                        className="btn btn-secondary"
                        disabled={page === 0}
                        onClick={() =>
                            setPage(page - 1)
                        }
                    >

                        Previous

                    </button>

                    <span>

                        Page {page + 1} of {totalPages}

                    </span>

                    <button
                        className="btn btn-secondary"
                        disabled={page + 1 >= totalPages}
                        onClick={() =>
                            setPage(page + 1)
                        }
                    >

                        Next

                    </button>

                </div>

                <hr className="my-5"/>

                <h4>Mini Statement</h4>

                <table className="table table-sm table-striped">

                    <thead>

                        <tr>

                            <th>Type</th>
                            <th>Amount</th>
                            <th>Date</th>

                        </tr>

                    </thead>

                    <tbody>

                        {miniStatement.map(transaction => (

                            <tr
                                key={transaction.transactionId}
                            >

                                <td>
                                    {transaction.transactionType}
                                </td>

                                <td>
                                    ₹ {transaction.amount}
                                </td>

                                <td>
                                    {transaction.transactionDate}
                                </td>

                            </tr>

                        ))}

                    </tbody>

                </table>

                <hr className="my-5"/>

                <button
                    className="btn btn-danger"
                    onClick={handleDownload}
                >

                    Download PDF Statement

                </button>

            </div>

        </>

    );

}

export default Transactions;