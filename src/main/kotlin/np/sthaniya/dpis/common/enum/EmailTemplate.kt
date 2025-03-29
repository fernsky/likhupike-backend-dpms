package np.sthaniya.dpis.common.enum

import np.sthaniya.dpis.common.template.BaseEmailTemplate

enum class EmailTemplate(val subject: String, val template: String) {
    PASSWORD_RESET(
        subject = "Reset Your Password",
        template = BaseEmailTemplate.wrap(
            "Password Reset",
            """
                <p>Hello,</p>
                <p>Click the button below to reset your password:</p>
                <div style="text-align: center; margin: 2rem 0;">
                    <a href="%resetLink%" style="
                        display: inline-block;
                        background: #123772;
                        color: white;
                        padding: 12px 32px;
                        text-decoration: none;
                        border-radius: 24px;
                        font-weight: 500;
                        font-size: 1rem;
                        box-shadow: 0 2px 6px rgba(11, 31, 66, 0.12);
                    ">Reset Password</a>
                </div>
                <p style="color: #546e7a; font-size: 0.9rem;">This link will expire in 15 minutes.</p>
            """
        )
    ),

    WELCOME(
        subject = "Welcome to dpis",
        template = BaseEmailTemplate.wrap(
            "Welcome",
            """
                <p>Hello,</p>
                <p>Your account has been created and is pending approval.</p>
                <p>You'll receive login instructions once your account is approved.</p>
                <p style="color: #546e7a; font-size: 0.9rem;"><strong>Email:</strong> %email%</p>
            """
        )
    ),

    ACCOUNT_APPROVED(
        subject = "Account Approved",
        template = BaseEmailTemplate.wrap(
            "Account Approved",
            """
                <p>Hello,</p>
                <p>Your account has been approved and is ready to use.</p>
                <div style="text-align: center; margin: 2rem 0;">
                    <a href="%loginLink%" style="
                        display: inline-block;
                        background: #123772;
                        color: white;
                        padding: 12px 32px;
                        text-decoration: none;
                        border-radius: 24px;
                        font-weight: 500;
                        font-size: 1rem;
                        box-shadow: 0 2px 6px rgba(11, 31, 66, 0.12);
                    ">Login Now</a>
                </div>
            """
        )
    ),

    ACCOUNT_CREATED(
        subject = "Account Created",
        template = BaseEmailTemplate.wrap(
            "Account Created",
            """
                <p>Hello,</p>
                <p>An account has been created for you in the Likhu Pike Digital Profile Information System.</p>
                <p>Set your password to access your account:</p>
                <div style="text-align: center; margin: 2rem 0;">
                    <a href="%resetLink%" style="
                        display: inline-block;
                        background: #123772;
                        color: white;
                        padding: 12px 32px;
                        text-decoration: none;
                        border-radius: 24px;
                        font-weight: 500;
                        font-size: 1rem;
                        box-shadow: 0 2px 6px rgba(11, 31, 66, 0.12);
                    ">Set Password</a>
                </div>
                <p style="color: #546e7a; font-size: 0.9rem;">This link will expire in 24 hours.</p>
            """
        )
    ),

    PASSWORD_RESET_OTP(
        subject = "Password Reset Code",
        template = BaseEmailTemplate.wrap(
            "Reset Code",
            """
                <p>Hello,</p>
                <p>Here is your password reset code:</p>
                <div style="
                    text-align: center;
                    margin: 2rem 0;
                    background: rgba(18, 55, 114, 0.05);
                    padding: 1rem;
                    border-radius: 12px;
                    font-size: 1.5rem;
                    letter-spacing: 0.1rem;
                    font-weight: bold;
                    color: #123772;
                ">%otp%</div>
                <p style="color: #546e7a; font-size: 0.9rem;">This code will expire in 15 minutes.</p>
            """
        )
    ),

    PASSWORD_RESET_SUCCESS(
        subject = "Password Reset Complete",
        template = BaseEmailTemplate.wrap(
            "Password Reset Complete",
            """
                <p>Hello,</p>
                <p>Your password has been successfully reset.</p>
                <div style="text-align: center; margin: 2rem 0;">
                    <a href="%loginLink%" style="
                        display: inline-block;
                        background: #123772;
                        color: white;
                        padding: 12px 32px;
                        text-decoration: none;
                        border-radius: 24px;
                        font-weight: 500;
                        font-size: 1rem;
                        box-shadow: 0 2px 6px rgba(11, 31, 66, 0.12);
                    ">Login Now</a>
                </div>
            """
        )
    )
}
