package np.sthaniya.dpis.common.enum

import np.sthaniya.dpis.common.template.BaseEmailTemplate

enum class EmailTemplate(val subject: String, val template: String) {
    PASSWORD_RESET(
        subject = "Reset Your Password - Likhu Pike Digital Profile System",
        template = BaseEmailTemplate.wrap(
            "Password Reset Request",
            """
                <p>Hello,</p>
                <p>We received a request to reset your password. Click the button below to reset it:</p>
                
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
                        transition: all 0.3s ease;
                        box-shadow: 0 2px 6px rgba(11, 31, 66, 0.12);
                    ">Reset Password</a>
                </div>
                
                <div style="
                    margin: 1.5rem 0;
                    padding: 1rem;
                    background: rgba(0, 0, 0, 0.03);
                    border-radius: 12px;
                    word-break: break-all;
                ">
                    <p style="margin: 0; font-size: 0.9rem;">%resetLink%</p>
                </div>
                
                <p><strong>Note:</strong> This link will expire in 15 minutes.</p>
                <p>If you didn't request this, please ignore this email.</p>
            """
        )
    ),

    WELCOME(
        subject = "Welcome to Likhu Pike Digital Profile System",
        template = BaseEmailTemplate.wrap(
            "Welcome to dpis",
            """
                <p>Hello,</p>
                <p>Welcome to the Likhu Pike Digital Profile Information System. Your account has been created successfully and is pending approval.</p>
                <p>Once your account is approved, you will receive another email with login instructions.</p>
                
                <div style="
                    margin: 1.5rem 0;
                    padding: 1rem;
                    background: rgba(0, 0, 0, 0.03);
                    border-radius: 12px;
                ">
                    <p style="margin: 0; font-size: 0.9rem;"><strong>Email:</strong> %email%</p>
                </div>
                
                <p>If you have any questions, please contact our support team.</p>
            """
        )
    ),

    ACCOUNT_APPROVED(
        subject = "Your Account Has Been Approved - Likhu Pike Digital Profile System",
        template = BaseEmailTemplate.wrap(
            "Account Approved",
            """
                <p>Hello,</p>
                <p>Great news! Your account has been approved and is now ready to use.</p>
                
                <div style="text-align: center; margin: 2rem 0;">
                    <a href="%loginLink%" style="
                        display: inline-block;
                        background: #27ae60;
                        color: white;
                        padding: 12px 32px;
                        text-decoration: none;
                        border-radius: 24px;
                        font-weight: 500;
                        font-size: 1rem;
                        transition: all 0.3s ease;
                        box-shadow: 0 2px 6px rgba(11, 31, 66, 0.12);
                    ">Login Now</a>
                </div>
                
                <div style="
                    margin: 1.5rem 0;
                    padding: 1rem;
                    background: rgba(0, 0, 0, 0.03);
                    border-radius: 12px;
                ">
                    <p style="margin: 0; font-size: 0.9rem;"><strong>Email:</strong> %email%</p>
                </div>
                
                <p>If you have any questions or need assistance, please don't hesitate to contact our support team.</p>
            """
        )
    ),

    ACCOUNT_CREATED(
        subject = "Your Account Has Been Created - Likhu Pike dpis",
        template = BaseEmailTemplate.wrap(
            "Account Created",
            """
                <p>Hello,</p>
                <p>An account has been created for you in the Likhu Pike Digital Profile Information System.</p>
                <p>For security reasons, you need to set your own password before you can access your account.</p>
                
                <div style="text-align: center; margin: 2rem 0;">
                    <a href="%resetLink%" style="
                        display: inline-block;
                        background: #e74c3c;
                        color: white;
                        padding: 12px 32px;
                        text-decoration: none;
                        border-radius: 24px;
                        font-weight: 500;
                        font-size: 1rem;
                        transition: all 0.3s ease;
                        box-shadow: 0 2px 6px rgba(11, 31, 66, 0.12);
                    ">Set Your Password</a>
                </div>
                
                <div style="
                    margin: 1.5rem 0;
                    padding: 1rem;
                    background: rgba(0, 0, 0, 0.03);
                    border-radius: 12px;
                ">
                    <p style="margin: 0; font-size: 0.9rem;"><strong>Email:</strong> %email%</p>
                </div>
                
                <p>If you did not expect this account creation, please contact our support team immediately.</p>
                <p><strong>Note:</strong> This link will expire in 24 hours for security reasons.</p>
            """
        )
    ),

    PASSWORD_RESET_OTP(
        subject = "Your Password Reset OTP - Likhu Pike dpis",
        template = BaseEmailTemplate.wrap(
            "Password Reset Code",
            """
                <p>Hello,</p>
                <p>You have requested to reset your password. Here is your one-time password (OTP):</p>
                
                <div style="text-align: center; margin: 2rem 0;">
                    <div style="
                        background: #e8f4f8;
                        padding: 1rem;
                        border-radius: 12px;
                        font-size: 1.5rem;
                        letter-spacing: 0.1rem;
                        font-weight: bold;
                    ">
                        %otp%
                    </div>
                </div>
                
                <p><strong>This code will expire in 15 minutes.</strong></p>
                <p>If you did not request this password reset, please ignore this email or contact support if you have concerns.</p>
                
                <div style="
                    margin-top: 1.5rem;
                    padding: 1rem;
                    background: #fff3cd;
                    border-radius: 12px;
                    color: #856404;
                ">
                    <p style="margin: 0;"><strong>Security Tips:</strong></p>
                    <ul style="margin: 0.5rem 0 0 1rem;">
                        <li>Never share this OTP with anyone</li>
                        <li>Our staff will never ask for your OTP</li>
                        <li>Make sure you're on our official website</li>
                    </ul>
                </div>
            """
        )
    ),

    PASSWORD_RESET_SUCCESS(
        subject = "Password Reset Successful - Likhu Pike dpis",
        template = BaseEmailTemplate.wrap(
            "Password Reset Successful",
            """
                <p>Hello,</p>
                <p>Your password has been successfully reset.</p>
                
                <div style="text-align: center; margin: 2rem 0;">
                    <a href="%loginLink%" style="
                        display: inline-block;
                        background: #27ae60;
                        color: white;
                        padding: 12px 32px;
                        text-decoration: none;
                        border-radius: 24px;
                        font-weight: 500;
                        font-size: 1rem;
                        transition: all 0.3s ease;
                        box-shadow: 0 2px 6px rgba(11, 31, 66, 0.12);
                    ">Login Now</a>
                </div>
                
                <div style="
                    margin-top: 1.5rem;
                    padding: 1rem;
                    background: #fff3cd;
                    border-radius: 12px;
                    color: #856404;
                ">
                    <p style="margin: 0;"><strong>Important:</strong> If you did not perform this password reset, please contact our support team immediately.</p>
                </div>
            """
        )
    )
}
