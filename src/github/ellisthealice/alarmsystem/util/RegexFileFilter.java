/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package github.ellisthealice.alarmsystem.util;

/**
 *
 * @author EllisTheAlice
 */
public class RegexFileFilter implements java.io.FileFilter {

    final java.util.regex.Pattern pattern;

    public RegexFileFilter(String regex) {
        pattern = java.util.regex.Pattern.compile(regex);
    }

    @Override
    public boolean accept(java.io.File f) {
        return pattern.matcher(f.getName()).find();
    }

}