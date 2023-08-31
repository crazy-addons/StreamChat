package net.crazy.streamchat.core.commands;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import lombok.Getter;

public enum CommandVariables {
  /* Minecraft */
  HEALTH("health"),
  USERNAME("username"),
  VERSION("version");

  @Getter
  private final String identifier;

  CommandVariables(String identifier) {
    this.identifier = identifier;
  }

  private final VariableProviders variableProviders = new VariableProviders();

  public String getValue() {
    Method[] methods = variableProviders.getClass().getMethods();
    for (Method method : methods) {
      if (!method.isAnnotationPresent(VariableProvider.class))
        continue;

      VariableProvider annotation = method.getAnnotation(VariableProvider.class);
      String annotationIdentifier = annotation.identifier();
      if (!annotationIdentifier.equalsIgnoreCase(getIdentifier()))
        continue;

      try {
        return (String) method.invoke(variableProviders);
      } catch (IllegalAccessException | InvocationTargetException e) {
        throw new RuntimeException(e);
      }
    }

    return "{" + getIdentifier() + "}";
  }
}
