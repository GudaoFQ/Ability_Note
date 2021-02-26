```xml
location / {
   add_header Access-Control-Allow-Origin *;
   add_header Access-Control-Allow-Headers X-Requested-With;
   add_header Access-Control-Allow-Methods GET,POST,PUT,DELETE,OPTIONS;

   if ($request_method = 'OPTIONS') {
     return 204;
   }
}
```