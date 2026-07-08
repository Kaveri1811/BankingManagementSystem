import { Link, useNavigate } from "react-router-dom";

function Navbar() {

    const navigate = useNavigate();

    const logout = () => {

        localStorage.removeItem("token");
        localStorage.removeItem("username");
        localStorage.removeItem("accountNumber");

        navigate("/login");
    };

    return (

        <nav className="navbar navbar-expand-lg navbar-dark bg-dark">

            <div className="container">

                <Link className="navbar-brand" to="/dashboard">
                    🏦 Bank Management
                </Link>

                <button
                    className="navbar-toggler"
                    type="button"
                    data-bs-toggle="collapse"
                    data-bs-target="#navbarNav">

                    <span className="navbar-toggler-icon"></span>

                </button>

                <div className="collapse navbar-collapse" id="navbarNav">

                    <ul className="navbar-nav ms-auto">

                        <li className="nav-item">
                            <Link className="nav-link" to="/dashboard">
                                Dashboard
                            </Link>
                        </li>

                        <li className="nav-item">
                            <Link className="nav-link" to="/deposit">
                                Deposit
                            </Link>
                        </li>

                        <li className="nav-item">
                            <Link className="nav-link" to="/withdraw">
                                Withdraw
                            </Link>
                        </li>

                        <li className="nav-item">
                            <Link className="nav-link" to="/transfer">
                                Transfer
                            </Link>
                        </li>

                        <li className="nav-item">
                            <Link className="nav-link" to="/transactions">
                                Transactions
                            </Link>
                        </li>

                        <li className="nav-item">

                        <Link
                            className="nav-link"
                            to="/create-account">

                            Create Account

                        </Link>

                    </li>

                        <li className="nav-item">
                            <button
                                className="btn btn-danger ms-3"
                                onClick={logout}>
                                Logout
                            </button>
                        </li>

                    </ul>

                </div>

            </div>

        </nav>

    );
}

export default Navbar;