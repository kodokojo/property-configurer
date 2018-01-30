![Kodo Kojo Logo](doc/images/logo-kodokojo-baseline-black1.png)

---

Kodo Kojo allows you to get a full out-of-the-box software factory.

This repository provide a small tool which allow us to configure our service and use those configuration in an smart and easy way. 
This little library is developed as a Java 9 module. 

License
------

`Kodo Kojo` is licensed under [GNU General Public License v3](http://www.gnu.org/licenses/gpl-3.0.en.html).

[![](https://img.shields.io/badge/License-GPLv3-blue.svg?style=flat)](http://www.gnu.org/licenses/gpl-3.0.en.html)

## Stay tuned

Stay tuned by following us on:

* Our Website http://kodokojo.io
* Twitter : [@kodokojo](http://twitter.com/kodokojo)
* Gitter : [Gitter](https://gitter.im/kodokojo/kodokojo) 

## Quickstart


### Maven configuration

Add following dependency in your `pom.xml` :
```
<dependency>
    <groupId>io.kodokojo</groupId>
    <artifactId>property-configurer</artifactId>
    <version>0.2.0</version>
</dependency>
```

### Use it

#### Create your Property configuration interface

Create an interface `ApplicationConfiguration` which will be used in your code :
```java
interface ApplicationConfiguration extends PropertyConfig {
        @Key("elasticsearch.host")
        String elasticsearchHost();
        
        @Key(value = "elasticsearch.port", defaultValue = "9200")
        Integer elasticsearchPort();
}
```

In thos sample, we provide the value of method `elasticsearchHost` via the System Env `elasticsearch_host`.
Create a PropertyResolver:
```java
PropertyResolver resolver = new PropertyResolver(new SystemEnvValueProvider());
ApplicationConfiguration proxy = resolver.createProxy(ApplicationConfiguration.class);
```

And enjoy :

```java
System.out.println("Elasticsearch host : " + proxy.elasticsearchHost());
```

Other property provider are available in this library, or you can provide yours implementing `PropertyValueProvider` interface.

## Technology inside

* [Java 9](http://java.com)
* [Maven](https://maven.apache.org/)
* [Apache Commons](https://commons.apache.org/)

We use the following tests tools:

* [Jgiven](http://jgiven.org/)
* [Mockito](http://mockito.org/)
* [AssertJ](http://joel-costigliola.github.io/assertj/)

Thanks to all those Open source projects which made such a project possible!
