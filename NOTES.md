# Spring Core – Lecture Notes (IoC, Beans, DI, Bean Lifecycle)

> These notes go with the projects in this repo, in commit order:
>
> | # | Commit | Project | Topic |
> |---|--------|---------|-------|
> | 1 | `1150f76` first commit | `carproject`, `TemplateTax I` | IoC container, XML config, beans, `getBean()` |
> | 2 | `b6d09b8` Constructor dependency injection | `LaptopDealer` | Constructor injection (`<constructor-arg>`) |
> | 3 | `821ac12` setter injection | `LaptopDealer` | Setter injection (`<property>`) |
> | 4 | `238a8f6` bean lifecycle | `socialmedia` | Bean scopes (singleton/prototype), `init-method`, `destroy-method` |
>
> Stack: Spring Boot 4.1.1 parent, Java 21. The Boot starters pull in `spring-context`, which has
> `ClassPathXmlApplicationContext`. `@SpringBootApplication` is commented out on purpose: these
> lectures use **plain Spring Core with XML**, not Boot auto-configuration.

---

## 0. Why Spring? The problem it solves

Without Spring, a class creates its own dependencies:

```java
public class Dell {
    Processors processors = new i5();   // hard-coded, tightly coupled
}
```

Problems:
- **Tight coupling**: `Dell` is tied to `i5`. To switch to `i7` you have to edit and recompile `Dell`.
- **Hard to test**: you can't pass in a fake processor.
- **Object creation is spread everywhere**: every class calls `new` on its own.

Spring moves the job of **creating objects and wiring them together** out of your classes and into a
**container**. Your classes only declare *what they need* (usually as an interface), and the container
supplies it.

---

## 1. Lecture 1 – IoC Container & Beans (`carproject`)

### 1.1 Key terms

| Term | Meaning |
|------|---------|
| **IoC (Inversion of Control)** | Control over creating objects and managing their lifecycle is *inverted*: it moves from your code to the framework. You don't call `new`; Spring does. |
| **IoC Container** | The Spring object that creates, configures, stores, and destroys objects. In code this is the `ApplicationContext`. |
| **Bean** | Any object that the Spring container creates and manages. |
| **Bean definition / configuration metadata** | The instructions telling the container *which* beans to create and *how*. These can be XML (this course so far), annotations, or Java `@Configuration` classes. |
| **DI (Dependency Injection)** | The *technique* Spring uses to implement IoC: it **injects** a bean's dependencies into it (through a constructor or setter). |

> IoC is the **principle**; DI is the **pattern/implementation** of that principle.

### 1.2 BeanFactory vs ApplicationContext

| `BeanFactory` | `ApplicationContext` |
|---------------|----------------------|
| The basic container interface | A sub-interface of `BeanFactory` with more features |
| **Lazy**: a bean is created only when you ask for it | **Eager** for singletons: they're created when the context starts |
| No event handling, i18n, or AOP integration | Supports events, i18n (`MessageSource`), AOP, and annotation processing |
| Rarely used directly today | **What you'll actually use** |

Common `ApplicationContext` implementations:
- `ClassPathXmlApplicationContext`: reads XML from the **classpath** (`src/main/resources`). **Used in this course.**
- `FileSystemXmlApplicationContext`: reads XML from a file system path.
- `AnnotationConfigApplicationContext`: reads `@Configuration` Java classes (next topic: annotations).

### 1.3 Program to an interface

```java
public interface Car {
    public void showDetails();
}

public class FamilyCar    implements Car { public void showDetails(){ System.out.println("This is FamilyCar"); } }
public class SportsCar    implements Car { ... }
public class CyberTruckCar implements Car { ... }
```

The client code (`main`) only knows about `Car`. Which concrete class it gets is decided by the
**XML configuration**. That's loose coupling.

### 1.4 The XML configuration file

`src/main/resources/ApplicationContext.xml`:

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xsi:schemaLocation="
        http://www.springframework.org/schema/beans
        http://www.springframework.org/schema/beans/spring-beans.xsd">

    <bean id="family"     class="com.example.carproject.FamilyCar"></bean>
    <bean id="sports"     class="com.example.carproject.SportsCar"></bean>
    <bean id="cybertruck" class="com.example.carproject.CyberTruckCar"></bean>
</beans>
```

- `<beans>`: the root element. The `xmlns` / `schemaLocation` lines tell the parser which schema
  (XSD) to validate against. Copy this header as-is.
- `<bean>`: one object definition.
  - `id`: a unique name you use to look the bean up.
  - `class`: the **fully qualified class name** (package + class). Spring creates it by reflection,
    which by default needs a **no-arg constructor**.
- `<bean ...></bean>` and `<bean .../>` (self-closing) mean the same thing.

### 1.5 Loading the container and getting beans

```java
ClassPathXmlApplicationContext context =
        new ClassPathXmlApplicationContext("ApplicationContext.xml");

Car car = (Car) context.getBean(beanId);   // beanId = "family" / "sports" / "cybertruck"
car.showDetails();
```

What happens step by step:
1. `new ClassPathXmlApplicationContext(...)` finds the XML on the classpath and parses it.
2. Spring builds a `BeanDefinition` for each `<bean>`.
3. All **singleton** beans are instantiated **right away** (eager loading).
4. `getBean("family")` returns the stored object.

Ways to call `getBean`:

```java
Car c1 = (Car) context.getBean("family");          // by id, needs a cast
Car c2 = context.getBean("family", Car.class);     // by id + type, no cast (preferred)
Car c3 = context.getBean(Car.class);               // by type: FAILS here, 3 beans implement Car
                                                   // -> NoUniqueBeanDefinitionException
```

That third case is why the commented-out `context.getBean(Brand.class)` in `LaptopDealer` would fail:
there are several `Brand` beans.

### 1.6 Runtime selection example (carproject)

The user enters 1/2/3, the code maps it to a bean id with a `switch`, then calls `getBean(beanId)`.
To add a new car type you only add a class and one `<bean>` line. The client logic barely changes.

**Gotcha:** if the user types something other than 1-3, `beanId` stays `""` and `getBean("")` throws
`NoSuchBeanDefinitionException`. Add a `default:` case.

### 1.7 `TemplateTax I` (assignment template)

This is an exercise skeleton:
- `Tax` interface: `setTaxableAmount`, `calculateTaxAmount`, `getTaxAmount`, `getTaxType`,
  `isTaxPayed`, `payTax`.
- `IncomeTax` (tax slabs) and `PropertyTax` (5% of property value) are meant to implement it.
- `TaxApplication.main` should load `applicationContext.xml` with `ClassPathXmlApplicationContext`.

Its current state, in case you want to finish it:
- `IncomeTax.java` has **no `package` line** and **no fields** (`taxAmount`, `isTaxPayed`), so it won't compile.
- `PropertyTax` has only TODO comments.
- `applicationContext.xml` was copied from carproject and still points to `com.example.carproject.*`
  classes. It should define `com.example.tax.IncomeTax` and `com.example.tax.PropertyTax` beans.

---

## 2. Lecture 2 – Constructor Injection (`LaptopDealer`, commit `b6d09b8`)

### 2.1 The domain

```
Brand (interface)            Processors (interface)
 ├── Dell                     ├── i3
 ├── Macbook                  ├── i5
 └── Microsoftlaptop          └── i7
```

A laptop **has a** processor. `Brand` depends on `Processors`. That's the **dependency**.

### 2.2 Code

```java
public class Dell implements Brand {
    Processors processors;

    public Dell(Processors processors) {      // dependency comes in through the constructor
        this.processors = processors;
    }

    public void ShowDetails() {
        System.out.println("You have selected Dell Laptop" + processors.showProcessorsDetails());
    }
}
```

```xml
<bean id="i3" class="com.example.LaptopDealer.i3"/>
<bean id="i5" class="com.example.LaptopDealer.i5"/>
<bean id="i7" class="com.example.LaptopDealer.i7"/>

<bean id="dellwithi3" class="com.example.LaptopDealer.Dell">
    <constructor-arg ref="i3"/>
</bean>
```

- `<constructor-arg ref="i3"/>`: pass the bean with id `i3` as a constructor argument.
- `ref` = a reference to **another bean**. `value` = a literal (String, int, ...).

### 2.3 More `<constructor-arg>` options

```xml
<!-- literal value -->
<constructor-arg value="16"/>

<!-- several arguments: pick by index, name, or type -->
<constructor-arg index="0" ref="i5"/>
<constructor-arg index="1" value="16"/>

<constructor-arg name="processors" ref="i5"/>
<constructor-arg type="int" value="16"/>
```

### 2.4 How Spring handles it

1. It sees that `dellwithi3` needs `i3`, so it creates `i3` **first** (dependency order).
2. It calls `new Dell(i3Bean)` by reflection.
3. The finished bean is stored under the id `dellwithi3`.

### 2.5 One class, many beans

Here **9 beans** come from 3 classes × 3 processors (`dellwithi3`, `dellwithi5`, ... `microsoftlaptopwithi7`).
A bean is an **instance plus configuration**, not a class. The same class can be registered many
times with different ids and different dependencies.

Because `i3`, `i5`, `i7` are singletons, `dellwithi3`, `mackbookwithi3`, and `microsoftlaptopwithi3`
all share the **same `i3` object**.

### 2.6 Pros and cons of constructor injection

✅ The dependency is **mandatory**: the object can't exist without it, so no `NullPointerException`.
✅ The field can be `final`, so the object is **immutable**.
✅ The dependencies are obvious from the constructor signature, and it's easy to unit-test
   (`new Dell(new FakeProcessor())`).
✅ **Recommended by the Spring team** for required dependencies.
❌ Long constructors when there are many dependencies (often a sign the class does too much).
❌ Circular dependencies (A needs B, B needs A) **can't** be resolved: `BeanCurrentlyInCreationException`.

---

## 3. Lecture 3 – Setter Injection (`LaptopDealer`, commit `821ac12`)

### 3.1 What changed

The constructor was replaced by a setter:

```java
public class Dell implements Brand {
    Processors processors;

    public void setProcessors(Processors processors) {   // setter
        this.processors = processors;
    }
    ...
}
```

```xml
<bean id="dellwithi3" class="com.example.LaptopDealer.Dell">
    <property name="processors" ref="i3"></property>
</bean>
```

### 3.2 How `<property name="...">` maps to code (important)

`name="processors"` → Spring capitalises the first letter and adds `set` → calls **`setProcessors(...)`**.

- It's matched against the **setter method name**, *not* the field name (JavaBeans convention).
- If the setter is missing or misspelled you get
  `NotWritablePropertyException: Invalid property 'processors' of bean class [...Dell]`.
- That's why `Microsoftlaptop` was also changed in this commit: the field `processor` was renamed to
  `processors` so the setter and the XML match the other classes.

### 3.3 How Spring handles it

1. Spring calls the **no-arg constructor**: `new Dell()`. (Once the parameterised constructor was
   removed, Java supplies a default one again.)
2. Then it calls `setProcessors(i3Bean)`.

### 3.4 Injecting literal values and collections with setters

```xml
<bean id="laptop" class="...">
    <property name="ram" value="16"/>                 <!-- Spring converts "16" to int -->
    <property name="processors" ref="i5"/>
    <property name="ports">
        <list>
            <value>USB-C</value>
            <value>HDMI</value>
        </list>
    </property>
</bean>
```

Also available: `<set>`, `<map><entry key=".." value=".."/></map>`, `<props>`, `<null/>`.

### 3.5 Constructor vs setter injection

| | Constructor | Setter |
|---|---|---|
| XML tag | `<constructor-arg>` | `<property name="">` |
| When injected | While the object is being created | After the object is created (no-arg constructor first) |
| Dependency type | **Mandatory** | **Optional** (can be left out) |
| Immutability | Yes (`final` fields possible) | No (can be changed later) |
| Partial injection | Not possible | Possible, but unset fields stay `null` → risk of NPE |
| Circular dependencies | ❌ Fails | ✅ Can be resolved (for singletons) |
| Readability with many deps | Long constructor | One readable line per property |
| If both are used for the same field | — | **Setter wins** (it runs after the constructor) |

**Rule of thumb:** constructor injection for required dependencies, setter injection for optional ones
or ones that can change.

---

## 4. Lecture 4 – Bean Scopes & Bean Lifecycle (`socialmedia`, commit `238a8f6`)

### 4.1 The domain

```
User (interface) ──► SimpleUser      has-a PostList (setter injected)
PostList         ──► SimplePostList  holds List<Post>
Post             ──► Simple          holds a message
```

```xml
<bean id="post"     class="com.example.socialmedia.Simple"         scope="prototype"/>
<bean id="postList" class="com.example.socialmedia.SimplePostList"/>
<bean id="user"     class="com.example.socialmedia.SimpleUser"
      init-method="init" destroy-method="destroy">
    <property name="postList" ref="postList"/>
</bean>
```

### 4.2 Bean scopes

| Scope | Meaning | Where it works |
|-------|---------|----------------|
| **singleton** (default) | **One** instance per container. Every `getBean()` returns the same object. | Everywhere |
| **prototype** | A **new** instance on every `getBean()` call | Everywhere |
| request | One per HTTP request | Web apps |
| session | One per HTTP session | Web apps |
| application | One per `ServletContext` | Web apps |
| websocket | One per WebSocket session | Web apps |

**Why `post` must be `prototype`:** each time the user creates a post, the code calls
`context.getBean("post")`. If `post` were a singleton you'd get the **same** object back every time.
`setMessage()` would overwrite the old message, and the list would hold N references to one object,
so "see all posts" would print the latest message N times. With `prototype`, each post is a separate
object.

**Why `postList` and `user` are singletons:** there's one user and one list of posts for the
whole app run. Every `getBean("postList")` must return the same list.

Try it yourself:
```java
Post p1 = context.getBean("post", Post.class);
Post p2 = context.getBean("post", Post.class);
System.out.println(p1 == p2);   // prototype -> false, singleton -> true
```

Singleton vs prototype in more detail:

| | Singleton | Prototype |
|---|---|---|
| Created | When the container starts (eager) | Each time `getBean()` is called (lazy) |
| Init callback | ✅ Called | ✅ Called (for each new instance) |
| Destroy callback | ✅ Called on `context.close()` | ❌ **Never called**. Spring hands the object over and stops tracking it |
| State | Should be stateless or shared | Can hold per-use state |

> ⚠️ Spring's "singleton" means **one per container per bean id**, not the GoF Singleton pattern
> (one per JVM/classloader). Two `<bean>` definitions of the same class give two singletons.

> ⚠️ If you inject a prototype bean into a singleton, it's injected **only once**, when the singleton
> is created, so you effectively end up with one instance. That's why this project calls
> `context.getBean("post")` every time instead of injecting `Post` into `SimplePostList`.
> (Proper fixes: `ObjectFactory`/`Provider`, `@Lookup` method injection, or a scoped proxy.)

`lazy-init="true"` on a singleton delays its creation until the first `getBean()`.

### 4.3 The bean lifecycle

```
Container starts (new ClassPathXmlApplicationContext)
        │
        ▼
1. Read bean definitions (XML)
2. Instantiate          → constructor is called
3. Populate properties  → DI: setters / constructor args
4. Aware callbacks      → BeanNameAware, ApplicationContextAware (optional)
5. BeanPostProcessor.postProcessBeforeInitialization
6. INIT                 → @PostConstruct → InitializingBean.afterPropertiesSet() → init-method
7. BeanPostProcessor.postProcessAfterInitialization
        │
        ▼
8. Bean is READY. The app uses it (getBean, method calls ...)
        │
        ▼
context.close()  /  JVM shutdown hook
9. DESTROY              → @PreDestroy → DisposableBean.destroy() → destroy-method
   (singletons only)
```

In this project:
```java
public class SimpleUser implements User {
    public void init()    { System.out.println("Db connected successfully"); }
    public void destroy() { System.out.println("Db disconnected successfully"); }
}
```
- `init-method="init"`: runs **after** the dependencies are injected. At that point `postList` is
  already set, so this is the right place for setup work (open a DB connection, load a cache, check
  config). In the constructor the dependencies aren't there yet.
- `destroy-method="destroy"`: runs when the container shuts down. Use it for cleanup (close
  connections, files, thread pools).
- The method names are up to you. They must be `public void` with no arguments.
- What you'll see: "Db connected successfully" is printed **as soon as the context is created**,
  before the username prompt, because singletons are eager. "Db disconnected successfully" appears
  only when `context.close()` runs.

### 4.4 Three ways to define lifecycle callbacks

| Approach | Init | Destroy | Notes |
|---|---|---|---|
| XML attributes | `init-method="init"` | `destroy-method="destroy"` | No Spring code in your class (**used here**) |
| Spring interfaces | `InitializingBean.afterPropertiesSet()` | `DisposableBean.destroy()` | Ties your class to Spring. Not recommended |
| Annotations | `@PostConstruct` | `@PreDestroy` | `jakarta.annotation.*`. **Most common today** |

Default for every bean in the file: `<beans default-init-method="init" default-destroy-method="destroy">`.

### 4.5 Closing the context

- `ApplicationContext` (the interface) has **no** `close()` method.
  `ConfigurableApplicationContext` / `ClassPathXmlApplicationContext` / `AbstractApplicationContext` do.
  That's why the variable is declared as `ClassPathXmlApplicationContext`.
- `context.close()`: destroys singletons now.
- `context.registerShutdownHook()`: destroys them automatically when the JVM exits.
- `try (var ctx = new ClassPathXmlApplicationContext("...")) { ... }`: the context is
  `AutoCloseable`, so it closes automatically.

### 4.6 Gotchas in the current socialmedia code

1. **Option 3 (EXIT) doesn't exit.** `case 3` calls `context.close()` but has no `break`/`return`,
   and the `while(true)` loop keeps going. The menu shows again. If the user then picks 1, calling
   `getBean("post")` on a closed context throws `IllegalStateException`. Fix:
   ```java
   case 3: {
       context.close();
       return;          // or System.exit(0)
   }
   ```
2. `scanner.nextLine()` after `nextInt()` is needed to consume the leftover newline. It's correctly
   placed in case 1. Remember this pattern.
3. Menu typo: "Sell All your post" → "See All your posts".
4. `user.setPostList(postList)` inside case 1 isn't needed. Spring already injected the same singleton
   `postList` through `<property>`.

---

## 5. Common gotchas across the projects

| Issue | Where | Explanation |
|---|---|---|
| File name case | `LaptopDealer` loads `"applicationContext.xml"` but the file is `ApplicationContext.xml` | This works on Windows because its file system ignores case. It **fails on Linux or when run from a JAR** (`FileNotFoundException` / `BeanDefinitionStoreException`). Keep the names identical. |
| Missing `default` in the switch | carproject, LaptopDealer | An empty bean id → `NoSuchBeanDefinitionException` |
| `getBean(Interface.class)` with several implementations | LaptopDealer (commented out) | `NoUniqueBeanDefinitionException`. Use the id, `primary="true"`, or (with annotations) `@Qualifier`/`@Primary` |
| A class bean needs a no-arg constructor (unless `<constructor-arg>` is used) | all | Otherwise: `BeanInstantiationException: No default constructor found` |
| Wrong FQCN in `class=""` | TemplateTax XML | `ClassNotFoundException` / `CannotLoadBeanClassException` |
| Unused `import java.sql.SQLOutput;` | LaptopDealer | Harmless. Your IDE added it automatically |

## 6. Common exceptions cheat sheet

| Exception | Cause |
|---|---|
| `NoSuchBeanDefinitionException` | No bean with that id/type exists |
| `NoUniqueBeanDefinitionException` | `getBean(Type.class)` matched more than one bean |
| `BeanCreationException` | Something failed while creating a bean (look at the nested cause) |
| `BeanCurrentlyInCreationException` | Circular dependency with constructor injection |
| `NotWritablePropertyException` | `<property name="x">` has no matching `setX()` |
| `UnsatisfiedDependencyException` | A required dependency couldn't be resolved |
| `BeanDefinitionStoreException` | The XML is missing or invalid |
| `IllegalStateException: ... has been closed already` | `getBean()` was called after `context.close()` |

---

## 7. Interview / revision questions

1. What is IoC? How is it related to DI?
2. Difference between `BeanFactory` and `ApplicationContext`?
3. What is a Spring bean? Can one class have several beans? *(Yes: 9 beans from 3 classes in LaptopDealer.)*
4. Constructor vs setter injection: when do you use each one? Which one handles circular dependencies?
5. What does `<property name="processors">` call internally? *(`setProcessors()`)*
6. `ref` vs `value`?
7. What's the default bean scope? What does singleton mean in Spring compared with the GoF pattern?
8. Why is `post` a prototype in the socialmedia app? What would break if it were a singleton?
9. Is a prototype bean's destroy method called? *(No.)*
10. Put the bean lifecycle phases in order. Where do `init-method` and `destroy-method` fit?
11. Why do initialization work in `init-method` and not in the constructor?
12. Why can't you call `close()` on a variable typed as `ApplicationContext`?
13. What happens when you inject a prototype bean into a singleton bean?
14. What's eager vs lazy initialization? How do you make a singleton lazy?

---

## 8. Up next

The untracked folder `DIusingannontation (1)/` points to the next topic: **DI with annotations**
(`@Component`, `@Autowired`, `@Qualifier`, `@Primary`, `@Value`, `@Scope`, `@PostConstruct`/`@PreDestroy`,
`<context:component-scan>` / `@ComponentScan`, `@Configuration` + `@Bean`). Every XML concept above has
an annotation equivalent:

| XML | Annotation |
|---|---|
| `<bean id="x" class="...">` | `@Component("x")` on the class, or `@Bean` method |
| `<constructor-arg ref>` | `@Autowired` on constructor (optional if single constructor) |
| `<property ref>` | `@Autowired` on setter |
| choosing among multiple beans | `@Qualifier("i5")` / `@Primary` |
| `<property value="16">` | `@Value("16")` / `@Value("${prop}")` |
| `scope="prototype"` | `@Scope("prototype")` |
| `init-method` / `destroy-method` | `@PostConstruct` / `@PreDestroy` |
| `ClassPathXmlApplicationContext` | `AnnotationConfigApplicationContext` |
