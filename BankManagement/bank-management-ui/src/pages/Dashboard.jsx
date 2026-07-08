import { useEffect, useState } from "react";
import { getDashboard } from "../services/dashboardService";
import Navbar from "../components/Navbar";

function Dashboard() {

    const [dashboard, setDashboard] = useState(null);

    useEffect(() => {

        loadDashboard();

    }, []);

    const loadDashboard = async () => {

        try {

            const accountNumber =
                localStorage.getItem("accountNumber");

            const data = await getDashboard(accountNumber);

            setDashboard(data);

        } catch (error) {

            console.log(error);

        }

    };

    if (!dashboard) {

        return <h3 className="text-center mt-5">Loading...</h3>;

    }

    return (
        <>
        <Navbar />
       
        <div className="container mt-4">

            <h2>
                Welcome,
                {" "}
                {dashboard.customerName}
            </h2>

            <hr/>

            <div className="row">

                <div className="col-md-4">

                    <div className="card shadow">

                        <div className="card-body">

                            <h5>Account Number</h5>

                            <h4>{dashboard.accountNumber}</h4>

                        </div>

                    </div>

                </div>

                <div className="col-md-4">

                    <div className="card shadow">

                        <div className="card-body">

                            <h5>Account Type</h5>

                            <h4>{dashboard.accountType}</h4>

                        </div>

                    </div>

                </div>

                <div className="col-md-4">

                    <div className="card shadow">

                        <div className="card-body">

                            <h5>Current Balance</h5>

                            <h4>₹ {dashboard.currentBalance}</h4>

                        </div>

                    </div>

                </div>

            </div>

            <div className="mt-5">

                <h4>Recent Transactions</h4>

                <table className="table table-striped">

                    <thead>

                        <tr>

                            <th>Type</th>
                            <th>Amount</th>
                            <th>Description</th>
                            <th>Date</th>

                        </tr>

                    </thead>

                    <tbody>

                        {dashboard.recentTransactions.map(transaction => (

                            <tr key={transaction.transactionId}>

                                <td>{transaction.transactionType}</td>

                                <td>₹ {transaction.amount}</td>

                                <td>{transaction.description}</td>

                                <td>{transaction.transactionDate}</td>

                            </tr>

                        ))}

                    </tbody>

                </table>

            </div>

        </div>
         </>

    );

}

export default Dashboard;