/*What it is: Enum for standard errors the server can return.

Examples: NONE, AUTH_FAILED, NOT_AUTHORIZED, CONFLICT, INSUFFICIENT_FUNDS, etc.

Why it matters: Client can handle failures consistently (show dialog, retry, etc.).

 */

import java.io.Serializable;

public enum ErrorCode implements Serializable {
    NONE, AUTH_FAILED, NOT_AUTHORIZED, INVALID_INPUT, NOT_FOUND,
    CONFLICT, INSUFFICIENT_FUNDS, INTERNAL_ERROR
}
