package gakesson.util.misc;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Vector;

public final class ClassLoaderHelper {

	private ClassLoaderHelper() {
		// Nothing
	}

	public static void writeLoadedClassesToFile(String absoluteFilePath,
			List<Class<?>> loadedClasses) {

		try {
			File file = new File(absoluteFilePath);

			try (BufferedOutputStream outputStream = new BufferedOutputStream(
					new FileOutputStream(file), 1024 * 1024)) {
				byte[] lineSeparator = System.lineSeparator().getBytes(
						StandardCharsets.UTF_8);
				for (Class<?> loadedClass : loadedClasses) {
					outputStream.write(loadedClass.getName().trim().getBytes(
							StandardCharsets.UTF_8));
					outputStream.write(lineSeparator);
				}
			}
		} catch (Throwable t) {
			// Silent ignore
		}
	}

	public static void loadClassesFromFile(String absoluteFilePath,
			ClassLoader classLoader) {

		try {
			File file = new File(absoluteFilePath);
			List<String> classesToLoad = new ArrayList<>();

			try (BufferedReader inputStream = new BufferedReader(
					new FileReader(file))) {

				String classToLoad = inputStream.readLine();

				while (classToLoad != null) {
					classesToLoad.add(classToLoad.trim());
					classToLoad = inputStream.readLine();
				}
			}

			loadClasses(classesToLoad, classLoader);
		} catch (Throwable t) {
			// Silent ignore
		}
	}

	/**
	 * Loads the provided list of classes (fully qualified names) using
	 * {@link Class#forName(String, boolean, ClassLoader)}, i.e. by also
	 * initializing the class.
	 * 
	 * This method does not raise any kind of {@link Throwable}.
	 * 
	 * @param classesToLoad
	 * @param classLoader
	 */
	public static void loadClasses(List<String> classesToLoad,
			ClassLoader classLoader) {

		try {
			for (String classToLoad : classesToLoad) {
				try {
					Class.forName(classToLoad, true, classLoader);
				} catch (ClassNotFoundException e) {
					// Silent ignore
				}
			}

		} catch (Throwable t) {
			// Silent ignore
		}
	}

	/**
	 * Returns an unmodifiable view of the currently loaded classes by the
	 * provided {@link ClassLoader}, or {@link Collections#EMPTY_LIST} in case
	 * it was not possible to retrieve due to e.g. an error.
	 * 
	 * This method does not raise any kind of {@link Throwable}.
	 * 
	 * @param classLoader
	 * @return
	 */
	public static List<Class<?>> getLoadedClassesOrDefault(
			ClassLoader classLoader) {
		List<Class<?>> loadedClasses = Collections.emptyList();

		try {
			loadedClasses = getLoadedClassesOrNull(classLoader,
					classLoader.getClass());
			loadedClasses = Collections.unmodifiableList(new ArrayList<>(
					loadedClasses));
		} catch (Throwable t) {
			// Silent ignore
		}

		return loadedClasses;
	}

	@SuppressWarnings("unchecked")
	// Whatever
	private static Vector<Class<?>> getLoadedClassesOrNull(
			ClassLoader classLoader, Class<?> classToSearchIn) {
		Vector<Class<?>> loadedClasses = null;

		if (classToSearchIn != null) {

			try {
				Field loadedClassesField = classToSearchIn
						.getDeclaredField("classes");
				loadedClassesField.setAccessible(true);
				loadedClasses = (Vector<Class<?>>) loadedClassesField
						.get(classLoader);
			} catch (NoSuchFieldException | IllegalAccessException e) {
				loadedClasses = getLoadedClassesOrNull(classLoader,
						classToSearchIn.getSuperclass());
			}
		}

		return loadedClasses;
	}
}
