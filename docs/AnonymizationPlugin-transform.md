Anonymization Transform Plugin
===========
Transform plugin that allow us to encrypt/decrypt fields using [google 
tink](https://github.com/google/tink) library.

Usage Notes
-----------
This plugin allows us to encrypt and decrypt data using AEAD 
and Deterministic Aead. Each field we want to encrypt must have a category, 
and each category is associated with a key that would be used in 
the encrypt/decrypt process.

Plugin Configuration
---------------------

| Configuration | Required | Default | Description |
| :------------ | :------: | :----- | :---------- |
| **Mode** | **Y** | Encrypt | Whether we want to encrypt or to decrypt. |
| **Encrypt Algorithm** | **Y** | AEAD | The algorithm used to encrypt or to decrypt |
| **Field-category** | **Y** | N/A | Definition of the field to encrypt/decrypt and their category
| **Category-Key** | **Y** | N/A | Definition of the Key used to each category|

Build
-----
To build this plugin:

```
   mvn clean package
```    

The build will create a .jar and .json file under the ``target`` directory.
These files can be used to deploy your plugins.

Deployment
----------
You can deploy your plugins using the CDAP CLI:

    > load artifact <target/vf-cis-cms-df-pg-anonymization-<version>.jar config-file <target/vf-cis-cms-df-pg-anonymization<version>.json>

For example, if your artifact is named 'vf-cis-cms-df-pg-anonymization-1.0.0':

    > load artifact target/vf-cis-cms-df-pg-anonymization-1.0.0.jar config-file target/vf-cis-cms-df-pg-anonymization-1.0.0.json