package np.sthaniya.dpis.common.enum

import np.sthaniya.dpis.common.template.BaseEmailTemplate

enum class EmailTemplate(val subject: String, val template: String) {
    PASSWORD_RESET(
        subject = "Reset Your Password",
        template = BaseEmailTemplate.wrap(
            "Password Reset",
            """
                <p style="margin: 0 0 16px; color: #334155;">Hello,</p>
                <p style="margin: 0 0 24px; color: #334155;">Click the button below to reset your password:</p>
                <table width="100%" cellpadding="0" cellspacing="0" border="0">
                    <tr>
                        <td align="center" style="padding: 16px 0 32px;">
                            <table cellpadding="0" cellspacing="0" border="0">
                                <tr>
                                    <td align="center" style="
                                        background-color: #123772;
                                        border-radius: 8px;
                                    ">
                                        <a href="%resetLink%" style="
                                            display: inline-block;
                                            padding: 14px 32px;
                                            font-family: system-ui, -apple-system, 'Segoe UI', sans-serif;
                                            font-size: 15px;
                                            font-weight: 500;
                                            color: #ffffff;
                                            text-decoration: none;
                                        ">Reset Password</a>
                                    </td>
                                </tr>
                            </table>
                        </td>
                    </tr>
                </table>
                <p style="
                    margin: 0;
                    color: #64748b;
                    font-size: 14px;
                    letter-spacing: -0.01em;
                ">This link will expire in 15 minutes.</p>
            """
        )
    ),

    WELCOME(
        subject = "Welcome to dpis",
        template = BaseEmailTemplate.wrap(
            "Welcome",
            """
                <p style="margin: 0 0 16px; color: #334155;">Hello,</p>
                <p style="margin: 0 0 24px; color: #334155;">Your account has been created and is pending approval.</p>
                <p style="margin: 0 0 24px; color: #334155;">You'll receive login instructions once your account is approved.</p>
                <p style="
                    margin: 0;
                    color: #64748b;
                    font-size: 14px;
                "><strong>Email:</strong> %email%</p>
            """
        )
    ),

    ACCOUNT_APPROVED(
        subject = "Account Approved",
        template = BaseEmailTemplate.wrap(
            "Account Approved",
            """
                <p style="margin: 0 0 16px; color: #334155;">Hello,</p>
                <p style="margin: 0 0 24px; color: #334155;">Your account has been approved and is ready to use.</p>
                <table width="100%" cellpadding="0" cellspacing="0" border="0">
                    <tr>
                        <td align="center" style="padding: 16px 0 32px;">
                            <table cellpadding="0" cellspacing="0" border="0">
                                <tr>
                                    <td align="center" style="
                                        background-color: #123772;
                                        border-radius: 8px;
                                    ">
                                        <a href="%loginLink%" style="
                                            display: inline-block;
                                            padding: 14px 32px;
                                            font-family: system-ui, -apple-system, 'Segoe UI', sans-serif;
                                            font-size: 15px;
                                            font-weight: 500;
                                            color: #ffffff;
                                            text-decoration: none;
                                        ">Login Now</a>
                                    </td>
                                </tr>
                            </table>
                        </td>
                    </tr>
                </table>
            """
        )
    ),

    ACCOUNT_CREATED(
        subject = "Account Created",
        template = BaseEmailTemplate.wrap(
            "Account Created",
            """
                <p style="margin: 0 0 16px; color: #334155;">Hello,</p>
                <p style="margin: 0 0 24px; color: #334155;">An account has been created for you in the Likhu Pike Digital Profile Information System.</p>
                <p style="margin: 0 0 24px; color: #334155;">Set your password to access your account:</p>
                <table width="100%" cellpadding="0" cellspacing="0" border="0">
                    <tr>
                        <td align="center" style="padding: 16px 0 32px;">
                            <table cellpadding="0" cellspacing="0" border="0">
                                <tr>
                                    <td align="center" style="
                                        background-color: #123772;
                                        border-radius: 8px;
                                    ">
                                        <a href="%resetLink%" style="
                                            display: inline-block;
                                            padding: 14px 32px;
                                            font-family: system-ui, -apple-system, 'Segoe UI', sans-serif;
                                            font-size: 15px;
                                            font-weight: 500;
                                            color: #ffffff;
                                            text-decoration: none;
                                        ">Set Password</a>
                                    </td>
                                </tr>
                            </table>
                        </td>
                    </tr>
                </table>
                <p style="
                    margin: 0;
                    color: #64748b;
                    font-size: 14px;
                    letter-spacing: -0.01em;
                ">This link will expire in 24 hours.</p>
            """
        )
    ),

    PASSWORD_RESET_OTP(
        subject = "Password Reset Code",
        template = BaseEmailTemplate.wrap(
            "Reset Code",
            """
                <p style="margin: 0 0 16px; color: #334155;">Hello,</p>
                <p style="margin: 0 0 24px; color: #334155;">Here is your password reset code:</p>
                <table width="100%" cellpadding="0" cellspacing="0" border="0">
                    <tr>
                        <td align="center" style="padding: 16px 0 32px;">
                            <table cellpadding="0" cellspacing="0" border="0">
                                <tr>
                                    <td align="center" style="
                                        background-color: rgba(18, 55, 114, 0.05);
                                        border-radius: 8px;
                                        padding: 14px 32px;
                                        font-family: system-ui, -apple-system, 'Segoe UI', sans-serif;
                                        font-size: 24px;
                                        font-weight: bold;
                                        color: #123772;
                                    ">
                                        %otp%
                                    </td>
                                </tr>
                            </table>
                        </td>
                    </tr>
                </table>
                <p style="
                    margin: 0;
                    color: #64748b;
                    font-size: 14px;
                    letter-spacing: -0.01em;
                ">This code will expire in 15 minutes.</p>
            """
        )
    ),

    PASSWORD_RESET_SUCCESS(
        subject = "Password Reset Complete",
        template = BaseEmailTemplate.wrap(
            "Password Reset Complete",
            """
                <p style="margin: 0 0 16px; color: #334155;">Hello,</p>
                <p style="margin: 0 0 24px; color: #334155;">Your password has been successfully reset.</p>
                <table width="100%" cellpadding="0" cellspacing="0" border="0">
                    <tr>
                        <td align="center" style="padding: 16px 0 32px;">
                            <table cellpadding="0" cellspacing="0" border="0">
                                <tr>
                                    <td align="center" style="
                                        background-color: #123772;
                                        border-radius: 8px;
                                    ">
                                        <a href="%loginLink%" style="
                                            display: inline-block;
                                            padding: 14px 32px;
                                            font-family: system-ui, -apple-system, 'Segoe UI', sans-serif;
                                            font-size: 15px;
                                            font-weight: 500;
                                            color: #ffffff;
                                            text-decoration: none;
                                        ">Login Now</a>
                                    </td>
                                </tr>
                            </table>
                        </td>
                    </tr>
                </table>
            """
        )
    )
}
