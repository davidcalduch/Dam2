package com.example.imagenscalerfx2.utils;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.EnumSet;
import javax.imageio.ImageIO;

public class IOUtils {
    public static void resize(String inputImagePath, String outputImagePath, int scaledWidth, int scaledHeight) {
        try {
            File inputFile = new File(inputImagePath);

            // Verificar existencia y validez del archivo de entrada
            if (!inputFile.exists() || !inputFile.isFile()) {
                System.err.println("Error: El archivo de entrada no existe o no es válido.");
                System.err.println("Ruta del archivo: " + inputFile.getAbsolutePath());
                throw new RuntimeException("El archivo de entrada no existe o no es válido.");
            }

            System.out.println("Entrada: " + inputImagePath);
            System.out.println("Salida: " + outputImagePath);

            BufferedImage inputImage = ImageIO.read(inputFile);
            BufferedImage outputImage = new BufferedImage(scaledWidth, scaledHeight, inputImage.getType());

            Graphics2D g2d = outputImage.createGraphics();
            g2d.drawImage(inputImage, 0, 0, scaledWidth, scaledHeight, null);
            g2d.dispose();

            ImageIO.write(outputImage, "jpg", new File(outputImagePath));
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Error de lectura de imagen: " + e.getMessage(), e);
        }
    }
    public static void resize(String inputImagePath, String outputImagePath, double percent) {
        try {
            File inputFile = new File(inputImagePath);

            if (!inputFile.exists() || !inputFile.isFile()) {
                throw new RuntimeException("El archivo de entrada no existe o no es válido.");
            }

            System.out.println("Entrada: " + inputImagePath);
            System.out.println("Salida: " + outputImagePath);

            BufferedImage inputImage = ImageIO.read(inputFile);

            int scaledWidth = (int) (inputImage.getWidth() * percent / 100);
            int scaledHeight = (int) (inputImage.getHeight() * percent / 100);

            resize(inputImagePath, outputImagePath, scaledWidth, scaledHeight);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Error de lectura de imagen: " + e.getMessage(), e);
        }
    }

    public static void deleteDirectory(Path path) {
        try {
            Files.walkFileTree(path, EnumSet.noneOf(FileVisitOption.class), Integer.MAX_VALUE, new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                    if (!dir.equals(path)) {
                        Files.delete(dir);
                    }
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    Files.delete(file);
                    return FileVisitResult.CONTINUE;
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
