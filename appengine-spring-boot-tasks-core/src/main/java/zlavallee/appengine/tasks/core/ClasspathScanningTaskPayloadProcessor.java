package zlavallee.appengine.tasks.core;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.boot.autoconfigure.AutoConfigurationPackages;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.context.support.ApplicationObjectSupport;
import org.springframework.core.env.Environment;
import org.springframework.core.env.StandardEnvironment;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.type.filter.AnnotationTypeFilter;

public class ClasspathScanningTaskPayloadProcessor extends ApplicationObjectSupport {

  private List<String> basePackages = new ArrayList<>();
  private Environment environment = new StandardEnvironment();
  private ResourceLoader resourceLoader = new DefaultResourceLoader();
  private BeanFactory beanFactory = new DefaultListableBeanFactory();

  public ClasspathScanningTaskPayloadProcessor() {
  }

  @Override
  public void initApplicationContext() {
    super.initApplicationContext();
    ApplicationContext applicationContext = obtainApplicationContext();

    setEnvironment(applicationContext.getEnvironment());
    setResourceLoader(applicationContext);
    setBeanFactory(applicationContext.getAutowireCapableBeanFactory());
  }

  public void setBasePackages(List<String> basePackages) {
    if (basePackages == null) {
      this.basePackages = new ArrayList<>();
    }
    this.basePackages = basePackages;
  }

  public void addBasePackage(String basePackage) {
    this.basePackages.add(basePackage);
  }

  public void addBasePackages(Collection<String> basePackages) {
    if (basePackages == null) {
      return;
    }

    this.basePackages.addAll(basePackages);
  }

  public Collection<Class<?>> findPayloadClasses() {
    ClassPathScanningCandidateComponentProvider provider = new ClassPathScanningCandidateComponentProvider(
        false);

    provider.setEnvironment(environment);
    provider.setResourceLoader(resourceLoader);
    provider.addIncludeFilter(new AnnotationTypeFilter(Payload.class));

    return getPackagesToScan().flatMap(
            basePackage -> provider.findCandidateComponents(basePackage).stream())
        .map(BeanDefinition::getBeanClassName)
        .filter(Objects::nonNull)
        .distinct()
        .map(this::getClass)
        .collect(Collectors.toList());
  }

  private Stream<String> getPackagesToScan() {
    return Stream.concat(
        AutoConfigurationPackages.get(beanFactory)
            .stream(), basePackages.stream());
  }

  private Class<?> getClass(String name) {
    try {
      return Class.forName(name);
    } catch (ClassNotFoundException e) {
      throw new IllegalArgumentException(
          "Error why registering task payload classes. No class found with name " + name, e);
    }
  }

  private void setEnvironment(Environment environment) {
    this.environment = environment;
  }

  private void setResourceLoader(ResourceLoader resourceLoader) {
    this.resourceLoader = resourceLoader;
  }

  private void setBeanFactory(BeanFactory beanFactory) throws BeansException {
    this.beanFactory = beanFactory;
  }
}
