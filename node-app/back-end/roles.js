const AccessControl = require("accesscontrol");
const ac = new AccessControl();

exports.roles = (function() {
  ac.grant("user")
    .readOwn("users")
    .updateOwn("users")
    .readOwn("posts")
    .updateOwn("posts")
    .deleteOwn("posts")

  ac.grant("admin")
    .extend("user")
    .readAny("users")
    .updateAny("users")
    .deleteAny("users")
    .readAny("posts")
    .updateAny("posts")
    .deleteAny("posts")

  return ac;
})();